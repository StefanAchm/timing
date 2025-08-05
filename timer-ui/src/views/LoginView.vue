<template>
  <v-container fluid class="fill-height">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card elevation="8" class="mx-auto login-card">
          <!-- Header -->
          <v-card-title class="justify-center py-6 card-header">
            <span class="text-h4 font-weight-light">Bouldercup Portal</span>
          </v-card-title>

          <!-- Login Form -->
          <v-card-text v-if="!isAuthenticated">
            <v-form
                ref="loginForm"
                v-model="isFormValid"
                @submit.prevent="handleLogin"
            >
              <v-text-field
                  v-model="credentials.username"
                  label="Benutzername"
                  prepend-icon="mdi-account"
                  outlined
                  :rules="validationRules.username"
                  :disabled="isLoading"
                  required
                  class="mb-3"
              />

              <v-text-field
                  v-model="credentials.password"
                  label="Passwort"
                  prepend-icon="mdi-lock"
                  :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  :type="showPassword ? 'text' : 'password'"
                  outlined
                  :rules="validationRules.password"
                  :disabled="isLoading"
                  required
                  class="mb-4"
                  @click:append="togglePasswordVisibility"
              />

              <v-alert
                  v-if="errorMessage"
                  type="error"
                  dismissible
                  v-model="showErrorAlert"
                  class="mb-4"
              >
                {{ errorMessage }}
              </v-alert>

              <v-btn
                  type="submit"
                  color="primary"
                  large
                  block
                  :loading="isLoading"
                  :disabled="!isFormValid || isLoading"
                  class="mb-3"
              >
                <v-icon left>mdi-login</v-icon>
                Einloggen
              </v-btn>
            </v-form>
          </v-card-text>

          <!-- Already Logged In State -->
          <v-card-text v-else class="text-center py-8">
            <v-icon size="64" color="success" class="mb-4">mdi-check-circle</v-icon>
            <h3 class="mb-8">Willkommen zurück, {{ currentUsername }}!</h3>
            <v-btn color="primary" @click="redirectToJudgePanel">
              Zum Bouldercup Portal
              <v-icon right>mdi-arrow-right</v-icon>
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import TimerApi from '@/plugins/timer-api'

export default {
  name: 'JudgeLogin',

  data() {
    return {
      credentials: {
        username: '',
        password: ''
      },
      errorMessage: '',
      isLoading: false,
      isFormValid: false,
      showErrorAlert: false,
      isAuthenticated: false,
      currentUsername: '',
      showPassword: false,

      validationRules: {
        username: [
          v => !!v || 'Benutzername ist erforderlich',
          v => v.length >= 3 || 'Benutzername muss mindestens 3 Zeichen lang sein',
        ],
        password: [
          v => !!v || 'Passwort ist erforderlich',
          v => v.length >= 4 || 'Passwort muss mindestens 4 Zeichen lang sein',
        ]
      }
    }
  },

  created() {
    this.checkAuthenticationStatus()
  },

  methods: {
    async handleLogin() {
      if (!this.validateForm()) return

      this.setLoadingState(true)
      this.clearError()

      try {
        await TimerApi.login(this.credentials.username, this.credentials.password)
        this.isAuthenticated = true
        this.currentUsername = this.credentials.username
        await this.$nextTick()
        this.redirectToJudgePanel()
      } catch (error) {
        this.handleLoginError()
      } finally {
        this.setLoadingState(false)
      }
    },

    validateForm() {
      return this.$refs.loginForm?.validate() ?? false
    },

    setLoadingState(loading) {
      this.isLoading = loading
    },

    clearError() {
      this.errorMessage = ''
      this.showErrorAlert = false
    },

    handleLoginError() {
      this.errorMessage = 'Login fehlgeschlagen. Bitte überprüfen Sie Ihre Anmeldedaten.'
      this.showErrorAlert = true
    },

    redirectToJudgePanel() {
      this.$router.push({name: 'Competitor Rounds'}).catch(err => {
        // Fallback to home route if JudgePanel route doesn't exist
        if (err.name === 'NavigationDuplicated') return
        this.$router.push('/')
      })
    },

    togglePasswordVisibility() {
      this.showPassword = !this.showPassword
    },

    checkAuthenticationStatus() {
      try {
        this.isAuthenticated = TimerApi.isAuthenticated()
        if (this.isAuthenticated) {
          const currentUser = TimerApi.getCurrentUser()
          this.currentUsername = currentUser?.username || 'Judge'
        }
      } catch (error) {
        console.error('Error checking auth status:', error)
        this.isAuthenticated = false
      }
    }
  }
}
</script>

<style scoped>
.fill-height {
  min-height: 100vh;
}

.login-card {
  border-radius: 16px !important;
  overflow: hidden;
}

.card-header {
  background: linear-gradient(45deg, var(--v-primary-base), var(--v-secondary-base));
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Improve form spacing and visual hierarchy */
.v-card-text {
  padding: 24px !important;
}

/* Add subtle animation for better UX */
.login-card {
  transition: transform 0.2s ease-in-out;
}

.login-card:hover {
  transform: translateY(-2px);
}

/* Better error alert styling */
.v-alert {
  border-radius: 8px;
}
</style>