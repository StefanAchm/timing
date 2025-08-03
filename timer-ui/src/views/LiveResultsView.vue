<template>
  <v-container fluid class="pa-2">
    <!-- Header Card -->
    <v-card class="mb-3" elevation="2">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-podium</v-icon>
        Ergebnisliste
        <!-- Live Update Indicator -->
        <v-spacer></v-spacer>
        <v-chip
            small
            color="success"
            text-color="white"
            class="ml-2 pulse-animation"
        >
          <v-icon left small>mdi-wifi</v-icon>
          Live
        </v-chip>
      </v-card-title>

      <!-- Gender Selection -->
      <v-card-text class="pb-2 pt-2">
        <div class="text-caption grey--text mb-2">Kategorie wählen:</div>
        <v-chip-group
            v-model="selectedGender"
            active-class="primary--text"
            mandatory
        >
          <v-chip
              value="HERREN"
              small
              outlined
              color="primary"
          >
            <v-icon left small>mdi-account</v-icon>
            Herren
          </v-chip>
          <v-chip
              value="DAMEN"
              small
              outlined
              color="primary"
          >
            <v-icon left small>mdi-account</v-icon>
            Damen
          </v-chip>
        </v-chip-group>
      </v-card-text>
    </v-card>

    <!-- Results List -->
    <v-card elevation="2" v-if="results?.length > 0">
      <v-card-title class="text-h6 py-2">
        <v-icon left>mdi-trophy</v-icon>
        Ergebnisse {{ selectedGender === 'HERREN' ? 'Herren' : 'Damen' }}
        <v-spacer></v-spacer>
        <v-chip small color="grey lighten-2" text-color="grey darken-2">
          {{ results.length }} Teilnehmer
        </v-chip>
      </v-card-title>

      <v-list dense>
        <template v-for="(result, index) in results">
          <v-list-item
              :key="index"
              :class="{
              'podium-first': result.rank === 1,
              'podium-second': result.rank === 2,
              'podium-third': result.rank === 3
            }"
              class="result-item"
          >
            <!-- Rank Badge -->
            <v-list-item-avatar size="40">
              <v-avatar
                  size="40"
                  :color="getRankColor(result.rank)"
              >
                <v-icon
                    v-if="result.rank <= 3"
                    color="white"
                    small
                >
                  {{ getRankIcon(result.rank) }}
                </v-icon>
                <span
                    v-else
                    class="white--text text-subtitle-2"
                >
                  {{ result.rank }}
                </span>
              </v-avatar>
            </v-list-item-avatar>

            <!-- Player Info -->
            <v-list-item-content>
              <v-list-item-title class="font-weight-bold mb-1">
                {{ result.firstName }} {{ result.lastName }}
              </v-list-item-title>

              <div class="result-details">
                <div class="text-caption grey--text mb-1">
                  <v-icon small color="grey">mdi-map-marker</v-icon>
                  {{ result.city }}
                  <span v-if="result.club" class="ml-2">
                    <v-icon small color="grey">mdi-account-group</v-icon>
                    {{ result.club }}
                  </span>
                </div>

                <div class="text-caption grey--text">
                  <span class="mr-3">
                    <v-icon small color="grey">mdi-numeric</v-icon>
                    Runde {{ result.lastRound }}
                  </span>
                  <span class="mr-3">
                    <v-icon small color="grey">mdi-hand-back-right</v-icon>
                    Griff {{ result.holdNumber }}{{ result.holdType }}
                  </span>
                  <span>
                    <v-icon small color="grey">mdi-counter</v-icon>
                    {{ result.tryNumber }} Versuch{{ result.tryNumber !== 1 ? 'e' : '' }}
                  </span>
                </div>
              </div>
            </v-list-item-content>

            <!-- Points -->
            <v-list-item-action>
              <div class="text-right">
                <v-chip
                    :color="getPointsColor(result.rank)"
                    :text-color="getPointsTextColor()"
                    class="font-weight-bold"
                >
                  {{ result.points }} Punkte
                </v-chip>
              </div>
            </v-list-item-action>
          </v-list-item>

          <v-divider v-if="index < results?.length - 1" :key="`divider-${index}`"></v-divider>
        </template>
      </v-list>
    </v-card>

    <!-- Empty State -->
    <v-card v-else-if="!isLoading" elevation="2" class="text-center pa-6">
      <v-icon size="64" color="grey lighten-2">mdi-trophy-outline</v-icon>
      <div class="text-h6 grey--text mt-3">
        Keine Ergebnisse verfügbar
      </div>
      <div class="text-body-2 grey--text">
        Für {{ selectedGender === 'HERREN' ? 'Herren' : 'Damen' }} sind noch keine Ergebnisse vorhanden.
      </div>
    </v-card>

    <!-- Loading State -->
    <v-card v-if="isLoading" elevation="2" class="text-center pa-6">
      <v-progress-circular
          indeterminate
          color="primary"
          size="48"
      ></v-progress-circular>
      <div class="text-body-1 mt-3">
        Lade Ergebnisse...
      </div>
    </v-card>

  </v-container>
</template>

<script>
import TimerApiService from "@/plugins/timer-api";

export default {

  data() {
    return {
      selectedGender: 'HERREN',
      results: [],
      isLoading: false,
      autoRefreshInterval: null,
    }
  },

  watch: {
    selectedGender() {
      this.refreshData()
    }
  },

  mounted() {
    this.refreshData()
    this.startAutoRefresh()
  },

  beforeDestroy() {
    this.stopAutoRefresh()
  },

  methods: {
    getRankColor(rank) {
      if (rank === 1) return 'amber darken-1' // Gold
      if (rank === 2) return 'grey' // Silver
      if (rank === 3) return 'deep-orange darken-1' // Bronze
      return 'primary'
    },

    getRankIcon(rank) {
      if (rank === 1) return 'mdi-trophy'
      if (rank === 2) return 'mdi-medal'
      if (rank === 3) return 'mdi-medal'
      return ''
    },

    getPointsColor(rank) {
      if (rank === 1) return 'amber darken-1'
      if (rank === 2) return 'grey'
      if (rank === 3) return 'deep-orange darken-1'
      return 'primary'
    },

    getPointsTextColor() {
      return 'white'
    },

    async refreshData() {
      this.isLoading = true

      try {
        // Replace this with your actual API call
        // Example: const response = await TimerApiService.getResults(this.selectedGender)
        // this.results = response.data

        // Mock data for demonstration
        // this.results = this.getMockResults()

        TimerApiService.getResultList(this.selectedGender).then( response => {
          this.results = response.data
        })

        // Sort by rank
        this.results.sort((a, b) => a.rank - b.rank)

      } catch (error) {
        console.error('Fehler beim Laden der Ergebnisse:', error)
        this.results = []
      } finally {
        this.isLoading = false
      }
    },

    // Mock data - replace with actual API call
    getMockResults() {
      const mockData = {
        herren: [
          {
            rank: 1,
            firstName: 'Stefan',
            lastName: 'Neumayr',
            city: 'Innsbruck',
            club: 'DAV Innsbruck',
            lastRound: 4,
            holdNumber: 26,
            holdType: 'T',
            tryNumber: 1,
            points: 28
          },
          {
            rank: 2,
            firstName: 'Gabriel',
            lastName: 'Pribil',
            city: 'Wien',
            club: 'ÖAV Wien',
            lastRound: 4,
            holdNumber: 25,
            holdType: 'Z',
            tryNumber: 3,
            points: 26
          },
          {
            rank: 3,
            firstName: 'Manuel',
            lastName: 'Tembler',
            city: 'Salzburg',
            club: 'DAV Salzburg',
            lastRound: 4,
            holdNumber: 24,
            holdType: 'T',
            tryNumber: 2,
            points: 24
          },
          {
            rank: 4,
            firstName: 'Jakob',
            lastName: 'Troger',
            city: 'Graz',
            club: 'ÖAV Graz',
            lastRound: 3,
            holdNumber: 22,
            holdType: 'Z',
            tryNumber: 4,
            points: 22
          },
          {
            rank: 5,
            firstName: 'Bernhard',
            lastName: 'Exenberger',
            city: 'Linz',
            club: 'DAV Linz',
            lastRound: 3,
            holdNumber: 20,
            holdType: 'T',
            tryNumber: 2,
            points: 20
          }
        ],
        damen: [
          {
            rank: 1,
            firstName: 'Anna',
            lastName: 'Müller',
            city: 'Wien',
            club: 'ÖAV Wien',
            lastRound: 4,
            holdNumber: 24,
            holdType: 'Z',
            tryNumber: 1,
            points: 25
          },
          {
            rank: 2,
            firstName: 'Lisa',
            lastName: 'Schmidt',
            city: 'Salzburg',
            club: 'DAV Salzburg',
            lastRound: 4,
            holdNumber: 22,
            holdType: 'T',
            tryNumber: 2,
            points: 23
          },
          {
            rank: 3,
            firstName: 'Sarah',
            lastName: 'Weber',
            city: 'Innsbruck',
            club: 'DAV Innsbruck',
            lastRound: 3,
            holdNumber: 20,
            holdType: 'Z',
            tryNumber: 3,
            points: 21
          }
        ]
      }

      return mockData[this.selectedGender] || []
    },

    startAutoRefresh() {
      this.autoRefreshInterval = setInterval(() => {
        this.refreshData()
      }, 30000) // 30 seconds
    },

    stopAutoRefresh() {
      if (this.autoRefreshInterval) {
        clearInterval(this.autoRefreshInterval)
        this.autoRefreshInterval = null
      }
    }
  }
}
</script>

<style scoped>
.result-item {
  min-height: 80px;
  padding: 12px 16px;
}

.podium-first {
  background: linear-gradient(45deg, rgba(255, 193, 7, 0.1), rgba(255, 193, 7, 0.05));
  border-left: 4px solid #ffc107;
}

.podium-second {
  background: linear-gradient(45deg, rgba(158, 158, 158, 0.1), rgba(158, 158, 158, 0.05));
  border-left: 4px solid #9e9e9e;
}

.podium-third {
  background: linear-gradient(45deg, rgba(255, 87, 34, 0.1), rgba(255, 87, 34, 0.05));
  border-left: 4px solid #ff5722;
}

.result-details {
  line-height: 1.2;
}

.v-card-title {
  word-break: normal;
}

@media (max-width: 600px) {
  .v-container {
    padding: 8px !important;
  }

  .v-card {
    margin-bottom: 12px;
  }

  .result-item {
    min-height: 72px;
    padding: 8px 12px;
  }
}

@keyframes pulse2 {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0.4);
  }
  50% {
    transform: scale(1.1);
    box-shadow: 0 0 10px 5px rgba(76, 175, 80, 0.2);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0);
  }
}

.pulse-animation {
  animation: pulse2 2s infinite;
}
</style>