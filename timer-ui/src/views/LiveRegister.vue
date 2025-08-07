<template>
  <v-container fluid class="pa-2">
    <!-- Header Card -->
    <v-card class="mb-3" elevation="2">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-account-plus</v-icon>
        Teilnehmer Anmeldung
      </v-card-title>

      <v-card-text class="pb-2 pt-2" v-if="registrationStatus">
        <div class="text-body-2 grey--text">
          Melde dich für den Wettkampf an. <br>
          Alle Felder mit * sind Pflichtfelder.
        </div>
      </v-card-text>
    </v-card>

    <v-expansion-panels
        v-if="registrationStatus"
        class="mt-3 mb-3"
        elevation="2"
    >
      <v-expansion-panel>
        <v-expansion-panel-header>
          <div class="d-flex align-center">
            <v-icon left color="primary" class="mr-3">mdi-cash</v-icon>
            <span class="text-h6 primary--text">Preise und Zahlung</span>
          </div>
        </v-expansion-panel-header>

        <v-expansion-panel-content>
          <!-- Pricing List -->
          <v-list dense class="pa-0">
            <v-list-item class="px-0">
              <v-list-item-content>
                <v-list-item-title class="text-body-1 font-weight-medium">
                  Unter 18 Jahre
                </v-list-item-title>
                <v-list-item-subtitle class="text-body-2 grey--text">
                  1× Getränk + 1× T-Shirt
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-action>
                <v-chip color="success" text-color="white" small>
                  25 €
                </v-chip>
              </v-list-item-action>
            </v-list-item>

            <v-list-item class="px-0">
              <v-list-item-content>
                <v-list-item-title class="text-body-1 font-weight-medium">
                  Ab 18 Jahre
                </v-list-item-title>
                <v-list-item-subtitle class="text-body-2 grey--text">
                  1× Getränk + 1× T-Shirt + Afterparty
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-action>
                <v-chip color="success" text-color="white" small>
                  35 €
                </v-chip>
              </v-list-item-action>
            </v-list-item>

          </v-list>

          <!-- Payment Info -->
          <v-alert
              type="info"
              class="mt-4 mb-2"
              dense
              outlined
              icon="mdi-cash-multiple"
          >
            <span class="text-body-2">Zahlung vor Ort in bar</span>
          </v-alert>

          <!-- T-Shirt Info -->
          <v-alert
              type="info"
              class="mb-2"
              dense
              outlined
              icon="mdi-tshirt-crew"
          >
            <span class="text-body-2">T-Shirts nur solange Vorrat reicht</span>
          </v-alert>

          <v-alert
              type="info"
              class="mb-0"
              dense
              outlined
              icon="mdi-account-multiple-check"
          >
            <span class="text-body-2">Rabatt für KIOT Mitglieder: 5 €</span>
          </v-alert>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>

    <!-- Registration Form Card -->
    <v-card
        v-if="registrationStatus"
        elevation="2">
      <v-card-title class="text-h6 py-3 primary--text">
        <v-icon left color="primary">mdi-clipboard-edit</v-icon>
        Persönliche Daten
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-3 pa-md-4">

        <v-form ref="registrationForm" v-model="isFormValid" lazy-validation>
          <v-container class="pa-0">

            <v-row no-gutters class="mx-n2">
              <!-- First Name -->
              <v-col cols="12" md="6" class="pa-2">
                <v-text-field
                    v-model="competitorLocal.firstName"
                    label="Vorname *"
                    :rules="nameRules"
                    required
                    outlined
                    prepend-inner-icon="mdi-account"
                    :error-messages="firstNameErrors"
                    @input="clearErrors('firstName')"
                />
              </v-col>

              <!-- Last Name -->
              <v-col cols="12" md="6" class="pa-2">
                <v-text-field
                    v-model="competitorLocal.lastName"
                    label="Nachname *"
                    :rules="nameRules"
                    required
                    outlined
                    prepend-inner-icon="mdi-account"
                    :error-messages="lastNameErrors"
                    @input="clearErrors('lastName')"
                />
              </v-col>
            </v-row>

            <v-row no-gutters class="mx-n2">
              <!-- Gender -->
              <v-col cols="12" md="6" class="pa-2">
                <v-select
                    v-model="competitorLocal.gender"
                    :items="genderOptions"
                    label="Kategorie *"
                    required
                    outlined
                    prepend-inner-icon="mdi-human-male-female"
                    :rules="genderRules"
                    :error-messages="genderErrors"
                    @input="clearErrors('gender')"
                >
                  <template v-slot:selection="{ item }">
                    <v-icon left small>{{ item.icon }}</v-icon>
                    {{ item.text }}
                  </template>
                  <template v-slot:item="{ item }">
                    <v-icon left small>{{ item.icon }}</v-icon>
                    {{ item.text }}
                  </template>
                </v-select>
              </v-col>

              <!-- Birthday -->
              <v-col cols="12" md="6" class="pa-2">
                <BirthdayPickerMenu
                    :date.sync="competitorLocal.dateOfBirth"
                    :error-messages="dateOfBirthErrors"
                    @input="clearErrors('dateOfBirth')"
                />
              </v-col>
            </v-row>

            <v-row no-gutters class="mx-n2">
              <!-- City -->
              <v-col cols="12" md="6" class="pa-2">
                <v-text-field
                    v-model="competitorLocal.city"
                    label="Stadt *"
                    :rules="cityRules"
                    required
                    outlined
                    prepend-inner-icon="mdi-city"
                    :error-messages="cityErrors"
                    @input="clearErrors('city')"
                />
              </v-col>

              <!-- Club -->
              <v-col cols="12" md="6" class="pa-2">
                <v-text-field
                    v-model="competitorLocal.club"
                    label="Verein"
                    outlined
                    prepend-inner-icon="mdi-account-group"
                    :error-messages="clubErrors"
                    @input="clearErrors('club')"
                />
              </v-col>
            </v-row>

            <!-- Privacy/Terms Checkbox -->
            <v-row no-gutters class="mx-n2">
              <v-col cols="12" class="pa-2">

                <v-checkbox
                    v-model="acceptTerms"
                    :rules="termsRules"
                    required
                >
                  <template v-slot:label>
                    <div class="text-body-2">

                      <!-- Dialog einbinden -->
                      <TermsDialog
                          :model-value.sync="showRules"
                          @rules-accepted="onRulesAccepted"
                          @rules-closed="onRulesClosed"
                      />

                      Ich akzeptiere die
                      <a href="#" @click.stop="showRules = true" class="primary--text">
                        Teilnahmebedingungen
                      </a>
                      und bin mit der Verarbeitung meiner Daten einverstanden. *
                    </div>
                  </template>
                </v-checkbox>

              </v-col>
            </v-row>

          </v-container>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <!-- Actions -->
      <v-card-actions class="pa-3 pa-md-4">
        <v-row no-gutters>
          <v-col cols="12" class="mb-2">
            <v-btn
                color="primary"
                block
                @click="submitRegistration"
                :loading="isSubmitting"
            >
              <v-icon left>mdi-check</v-icon>
              Anmeldung abschicken
            </v-btn>
          </v-col>
          <v-col cols="12">
            <v-btn
                color="grey"
                outlined
                block
                @click="resetForm(false)"
                :disabled="isSubmitting"
            >
              <v-icon left>mdi-refresh</v-icon>
              Zurücksetzen
            </v-btn>
          </v-col>
        </v-row>

      </v-card-actions>
    </v-card>

    <v-card v-else class="mt-3" elevation="2" color="grey lighten-4">
      <v-card-text class="text-center py-6">
        <v-icon size="64" color="grey">mdi-alert-circle</v-icon>
        <div class="text-h5 grey--text mt-3 font-weight-bold">
          Anmeldung geschlossen
        </div>
        <div class="text-body-1 mt-2">
          Die Anmeldung ist derzeit nicht möglich.
        </div>
      </v-card-text>
    </v-card>

    <!-- Success Message Card -->
    <v-card
        v-if="registrationSuccess"
        class="mt-3"
        elevation="2"
        color="green lighten-4"
    >
      <v-card-text class="text-center py-6">
        <v-icon size="64" color="green">mdi-check-circle</v-icon>
        <div class="text-h5 green--text mt-3 font-weight-bold">
          Anmeldung erfolgreich!
        </div>

        <div class="text-body-1 mt-2">
          Vielen Dank für deine Anmeldung <strong>{{ competitorSuccess.firstName }}</strong>. Du bist nun für den Wettkampf in der Kategorie <strong>{{ competitorSuccess.gender }}</strong>
          angemeldet.
        </div>

        <div class="text-body-1 mt-2">
          Deine Startnummer ist <strong>{{ competitorSuccess.startNumber }}</strong>
        </div>
<!--        <div class="text-body-2 grey&#45;&#45;text mt-2">-->
<!--          Weitere Details findest du in der Startliste.-->
<!--        </div>-->
<!--        <v-btn-->
<!--            color="primary"-->
<!--            class="mt-4"-->
<!--            @click="goToStartList"-->
<!--        >-->
<!--          <v-icon left>mdi-trophy</v-icon>-->
<!--          Zur Startliste-->
<!--        </v-btn>-->
      </v-card-text>
    </v-card>

  </v-container>

</template>

<script>
import BirthdayPickerMenu from "@/components/BirthdayPickerMenu.vue";
import TimerApiService from "@/plugins/timer-api";
import TermsDialog from "@/components/TermsDialog.vue";

export default {
  components: {
    TermsDialog,
    BirthdayPickerMenu
  },

  data() {
    return {
      isFormValid: false,
      isSubmitting: false,
      registrationSuccess: false,
      acceptTerms: false,
      showRules: false,

      competitorSuccess: {
        startNumber: null,
        firstName: '',
        lastName: '',
        gender: '',
        dateOfBirth: '',
        city: '',
        club: ''
      },

      competitorLocal: {
        firstName: '',
        lastName: '',
        gender: '',
        dateOfBirth: '',
        city: '',
        club: ''
      },

      registrationStatus: false,

      // Error messages
      firstNameErrors: [],
      lastNameErrors: [],
      genderErrors: [],
      dateOfBirthErrors: [],
      cityErrors: [],
      clubErrors: [],

      // Validation rules
      nameRules: [
        v => !!v || 'Dieses Feld ist erforderlich',
        v => (v && v.length >= 2) || 'Mindestens 2 Zeichen erforderlich',
        v => (v && v.length <= 50) || 'Maximal 50 Zeichen erlaubt'
      ],

      cityRules: [
        v => !!v || 'Stadt ist erforderlich',
        v => (v && v.length >= 2) || 'Mindestens 2 Zeichen erforderlich'
      ],

      genderRules: [
        v => !!v || 'Kategorie muss ausgewählt werden'
      ],

      termsRules: [
        v => !!v || 'Teilnahmebedingungen müssen akzeptiert werden'
      ],

      genderOptions: [
        {text: 'Herren', value: 'HERREN', icon: 'mdi-account'},
        {text: 'Damen', value: 'DAMEN', icon: 'mdi-account'}
      ]
    }
  },

  created() {
    this.init();
  },

  mounted() {
  },

  methods: {

    init() {
      TimerApiService.getRegistrationStatus()
          .then(response => {
            this.registrationStatus = response.data;
          })
          .catch(() => {
            this.$root.snackbar.showError({message: 'Fehler beim Laden des Registrierungsstatus.'});
          });
    },

    onRulesAccepted() {
      // set acceptTerms to true when rules are accepted
      this.acceptTerms = true;
    },
    onRulesClosed() {
      // Reset acceptTerms if rules dialog is closed without accepting
      this.acceptTerms = false;
    },

    clearErrors(field) {
      switch (field) {
        case 'firstName':
          this.firstNameErrors = [];
          break;
        case 'lastName':
          this.lastNameErrors = [];
          break;
        case 'gender':
          this.genderErrors = [];
          break;
        case 'dateOfBirth':
          this.dateOfBirthErrors = [];
          break;
        case 'city':
          this.cityErrors = [];
          break;
        case 'club':
          this.clubErrors = [];
          break;
      }
    },

    resetForm(registrationSuccess) {
      this.$refs.registrationForm.reset();
      this.competitorLocal = {
        firstName: '',
        lastName: '',
        gender: '',
        dateOfBirth: '',
        city: '',
        club: ''
      };
      this.acceptTerms = false;
      this.registrationSuccess = registrationSuccess;
      this.clearAllErrors();
    },

    clearAllErrors() {
      this.firstNameErrors = [];
      this.lastNameErrors = [];
      this.genderErrors = [];
      this.dateOfBirthErrors = [];
      this.cityErrors = [];
      this.clubErrors = [];
    },

    async submitRegistration() {

      this.registrationSuccess = false;

      // Validate form first
      if (!this.$refs.registrationForm.validate()) {
        this.$root.snackbar?.showError?.({
          message: 'Bitte überprüfe deine Eingaben'
        });
        return;
      }

      if (!this.acceptTerms) {
        this.$root.snackbar?.showError?.({
          message: 'Bitte akzeptiere die Teilnahmebedingungen'
        });
        return;
      }

      this.isSubmitting = true;

      try {
        await TimerApiService.register(this.competitorLocal)
            .then((response) => {
              if (response.data) {
                this.competitorSuccess = response.data;
              }
            });

        this.registrationSuccess = true;

        // Scroll to success message
        this.$nextTick(() => {
          const successCard = this.$el.querySelector('.green.lighten-4');
          if (successCard) {
            successCard.scrollIntoView({behavior: 'smooth', block: 'center'});
          }
        });

        this.resetForm(true);

      } catch (error) {

        // Handle specific errors
        if (error.response?.status === 409) {
          this.$root.snackbar?.showError?.({
            message: 'Ein Teilnehmer mit diesen Daten existiert bereits'
          });
        } else if (error.response?.status === 400) {
          this.$root.snackbar?.showError?.({
            message: 'Ungültige Eingabedaten. Bitte überprüfe deine Angaben.'
          });
        } else {
          this.$root.snackbar?.showError?.({
            message: 'Fehler bei der Anmeldung. Bitte versuche es erneut.'
          });
        }
      } finally {
        this.isSubmitting = false;
      }
    },

    goToStartList() {
      // Navigate to start list - adjust route as needed
      this.$router.push('/liveStart');
    }
  },

}
</script>

<style scoped>
.v-card-title {
  word-break: normal;
}

.pulse-animation {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0.4);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 0 10px 5px rgba(76, 175, 80, 0.2);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0);
  }
}

@media (max-width: 600px) {
  .v-container {
    padding: 8px !important;
  }

  .v-card {
    margin-bottom: 12px;
  }
}

/* Custom styling for better form appearance */
.v-text-field--outlined >>> .v-input__control > .v-input__slot {
  min-height: 56px;
}

.v-select--outlined >>> .v-input__control > .v-input__slot {
  min-height: 56px;
}

/* Success card animation */
.green.lighten-4 {
  animation: slideIn 0.5s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>