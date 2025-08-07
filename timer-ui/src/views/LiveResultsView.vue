<template>
  <v-container fluid class="pa-2">
    <!-- Header Card -->
    <v-card class="mb-3" elevation="2">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-podium</v-icon>
        Ergebnisse
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
      <v-card-text class="pb-2 pt-2 d-flex align-center">
        <div class="text-caption grey--text mr-3">Kategorie wählen:</div>
        <v-chip-group
            v-model="selectedGender"
            mandatory
            active-class="primary--text"
        >
          <v-chip value="HERREN">
            <v-icon left small>mdi-account</v-icon>
            Herren
          </v-chip>
          <v-chip value="DAMEN">
            <v-icon left small>mdi-account</v-icon>
            Damen
          </v-chip>
        </v-chip-group>

      </v-card-text>

      <v-card-text class="pb-2 pt-2 d-flex align-center">

        <!-- Search Bar -->

        <v-text-field
            v-model="searchQuery"
            label="Suche nach Name"
            clearable
            class="mb-3"
            hide-details
            outlined
            @input="filterResults"
        />

      </v-card-text>

    </v-card>



    <!-- Results List -->
    <v-card elevation="2" v-if="filteredResults?.length > 0">
      <v-card-title class="text-h6 py-2 primary--text">
        Ergebnisse {{ selectedGender === 'HERREN' ? 'Herren' : 'Damen' }}
        <v-spacer></v-spacer>
        <v-chip small color="grey lighten-2" text-color="grey darken-2">
          {{ filteredResults.length }} Teilnehmer
        </v-chip>
      </v-card-title>

      <v-list dense>
        <template v-for="(result, index) in filteredResults">
          <v-list-item
              :key="index"
              :class="{
              'podium-first': result.rank === 1,
              'podium-second': result.rank === 2,
              'podium-third': result.rank === 3
            }"
              class="result-item"
              @click="openCompetitorDialog(result)"
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
              <v-list-item-title class="font-weight-bold mb-2 text-h7">
                {{ result.firstName }} {{ result.lastName }}
              </v-list-item-title>
              <div class="result-details">
                <div class="text-caption grey--text mb-1">
                  <v-icon small color="grey">mdi-numeric</v-icon>
                  Runde {{ result.lastRound }}
                </div>
                <div class="text-caption grey--text mb-1">
                  <v-icon small color="grey">mdi-hand-back-right</v-icon>
                  Griff {{ result.holdNumber }} {{ result.holdType }}
                </div>
                <div class="text-caption grey--text">
                  <v-icon small color="grey">mdi-counter</v-icon>
                  {{ result.tryNumber }} Versuch{{ result.tryNumber !== 1 ? 'e' : '' }}
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
                  {{ result.points.toFixed(4) }}
                </v-chip>
              </div>
            </v-list-item-action>
          </v-list-item>

          <v-divider v-if="index < results?.length - 1" :key="`divider-${index}`"></v-divider>
        </template>
      </v-list>
    </v-card>

    <!-- Empty State -->
    <v-card v-else-if="!isLoading" class="mt-3 text-center" elevation="2" color="grey lighten-4">
      <v-card-text class="text-center py-6">
        <v-icon size="64" color="grey">mdi-trophy-outline</v-icon>
        <div class="text-h5 grey--text mt-3 font-weight-bold">
          Keine Ergebnisse verfügbar
        </div>
        <div class="text-body-1 mt-2">
          Für {{ selectedGender === 'HERREN' ? 'Herren' : 'Damen' }} sind noch keine Ergebnisse vorhanden.
        </div>
      </v-card-text>
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

    <!-- Competitor Details Dialog -->
    <CompetitorCardLive
        :dialog.sync="competitorDialog"
        :competitor="selectedCompetitor"
    />

  </v-container>
</template>

<script>
import TimerApiService from "@/plugins/timer-api";
import CompetitorCardLive from "@/components/competitor/CompetitorCardLive.vue";


export default {

  components: {CompetitorCardLive},

  data() {
    return {
      selectedGender: 'HERREN',
      results: [],
      filteredResults: [],
      searchQuery: '',
      isLoading: false,
      autoRefreshInterval: null,
      competitorDialog: false,
      selectedCompetitor: null,
    }
  },

  watch: {
    selectedGender() {
      this.refreshData()
    },
  },

  mounted() {
    this.refreshData()
    this.startAutoRefresh()
  },

  beforeDestroy() {
    this.stopAutoRefresh()
  },

  methods: {

    filterResults() {
      if(!this.searchQuery) {
        this.searchQuery = ''
      }
      const query = this.searchQuery.trim().toLowerCase()
      if (!query) {
        this.filteredResults = [...this.results]
      } else {
        this.filteredResults = this.results.filter(r =>
            `${r.firstName} ${r.lastName}`.toLowerCase().includes(query)
        )
      }
    },

    getRankColor(rank) {
      if (rank === 0) return 'grey lighten-2' // No rank
      if (rank === 1) return 'amber'   // Gold
      if (rank === 2) return 'secondPlace' // Silver
      if (rank === 3) return 'thirdPlace'  // Bronze
      return 'grey' // Default for others
    },

    getRankIcon(rank) {
      if (rank === 0) return 'DNS' // No rank
      if (rank === 1) return 'mdi-trophy'
      if (rank === 2) return 'mdi-medal'
      if (rank === 3) return 'mdi-medal'
      return ''
    },

    getPointsColor(rank) {
      if (rank === 0) return 'grey lighten-2' // No rank
      if (rank === 1) return 'firstPlace'   // Gold
      if (rank === 2) return 'secondPlace' // Silver
      if (rank === 3) return 'thirdPlace'  // Bronze
      return 'grey' // Default for others
    },

    getPointsTextColor() {
      return 'white'
    },

    async refreshData() {
      this.isLoading = true

      try {

        TimerApiService.getResultList(this.selectedGender).then(response => {
          this.results = response.data
          this.filterResults()
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

    startAutoRefresh() {
      this.autoRefreshInterval = setInterval(() => {
        this.refreshData()
      }, 30000)
    },

    openCompetitorDialog(competitor) {
      this.selectedCompetitor = competitor
      this.competitorDialog = true
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
  border-left: 4px solid var(--v-firstPlace-base);
}

.podium-second {
  background: linear-gradient(45deg, rgba(158, 158, 158, 0.1), rgba(158, 158, 158, 0.05));
  border-left: 4px solid var(--v-secondPlace-base);
}

.podium-third {
  background: linear-gradient(45deg, rgba(255, 87, 34, 0.1), rgba(255, 87, 34, 0.05));
  border-left: 4px solid var(--v-thirdPlace-base);
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