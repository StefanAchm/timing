<template>
  <v-container fluid class="pa-2">

    <!-- Header Card -->
    <v-card class="mb-3" elevation="2" v-if="$route.meta.type !== 'live'">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-trophy</v-icon>
        <span>
          Startliste
        </span>
        <!-- Live Indicator -->
        <v-spacer></v-spacer>
        <v-chip
            v-if="selectedView === 'live'"
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
            v-model="selectedView"
            mandatory
            @change="onViewChange"
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

      <v-divider></v-divider>

      <v-card-text class="pb-2 pt-2">
        <v-row no-gutters>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Teilnehmer</div>
            <div class="text-h6">{{ displayRound?.numberOfCompetitors || displayCompetitorRounds?.length || 0 }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Abgeschlossen</div>
            <div class="text-h6">{{ completedCount }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Griffe</div>
            <div class="text-h6">{{ displayRound?.maxHolds || '---' }}</div>
          </v-col>
        </v-row>
      </v-card-text>

    </v-card>

    <!-- Header Card -->
    <v-card class="mb-3" elevation="2" v-if="$route.meta.type === 'live'">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-trophy</v-icon>
        <span v-if="displayRound">
          Runde {{ displayRound?.roundNumber }} der {{ displayRound?.gender }}
        </span>
        <!-- Live Indicator -->
        <v-spacer></v-spacer>
        <v-chip
            v-if="selectedView === 'live'"
            small
            color="success"
            text-color="white"
            class="ml-2 pulse-animation"
        >
          <v-icon left small>mdi-wifi</v-icon>
          Live
        </v-chip>
      </v-card-title>

      <v-card-text class="pb-2 pt-2">
        <v-row no-gutters>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Teilnehmer</div>
            <div class="text-h6">{{ displayRound?.numberOfCompetitors || displayCompetitorRounds?.length || 0 }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Abgeschlossen</div>
            <div class="text-h6">{{ completedCount }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Griffe</div>
            <div class="text-h6">{{ displayRound?.maxHolds || '---' }}</div>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- Current Player Card (only in live mode) -->
    <v-card
        v-if="selectedView === 'live' && currentCompetitorRound"
        class="mb-3 current-player-card"
        elevation="4"
        color="green lighten-4"
    >
      <v-card-text class="text-center py-4">
        <v-avatar size="60" class="mb-2" color="green">
          <span class="white--text text-h5">{{ currentCompetitorRound.position }}</span>
        </v-avatar>
        <div class="text-h5 font-weight-bold mb-1">
          {{ currentCompetitorRound.competitor.firstName }} {{ currentCompetitorRound.competitor.lastName }}
        </div>
        <div class="text-subtitle-1 green--text">
          <v-icon color="green" small>mdi-clock</v-icon>
          Aktuell am Zug
        </div>
        <v-chip color="green" text-color="white" small class="mt-2">
          {{ currentCompetitorRound.score }} Punkte
        </v-chip>
      </v-card-text>
    </v-card>

    <!-- Competitors List -->
    <v-card elevation="2" v-if="displayCompetitorRounds?.length > 0">
      <v-card-title class="text-h6 py-2">
        <span>Startliste {{displayRound?.gender === 'HERREN' ? 'Herren' : 'Damen'}}</span>
      </v-card-title>

      <v-list dense>
        <template v-for="(competitorRound, index) in displayCompetitorRounds">
          <v-list-item
              :key="index"
              :class="{
              'current-player-item': competitorRound.isCurrent && selectedView === 'live',
              'completed-player': competitorRound.score !== null && !competitorRound.isCurrent
              }"
              @click="openCompetitorDialog(competitorRound)"
          >
            <!-- Position Badge -->
            <v-list-item-avatar size="36">
              <v-avatar
                  size="36"
                  :color="getAvatarColor(competitorRound)"
              >
                <span class="white--text text-subtitle-2">{{ index + 1 }}</span>
              </v-avatar>
            </v-list-item-avatar>

            <!-- Player Info -->
            <v-list-item-content>
              <v-list-item-title
                  :class="{ 'font-weight-bold': competitorRound.isCurrent && selectedView === 'live' }"
              >
                {{ competitorRound.competitor.firstName }} {{ competitorRound.competitor.lastName }}
              </v-list-item-title>
            </v-list-item-content>

            <!-- Score and Status -->
            <v-list-item-action v-if="selectedView === 'live'">
              <div class="text-right">
                <v-chip
                    small
                    :color="getScoreColor(competitorRound)"
                    :text-color="getScoreTextColor(competitorRound)"
                    class="mb-1"
                >
                  {{ competitorRound.score !== null ? competitorRound.score : '---' }} Punkte
                </v-chip>

                <!-- Status Icons -->
                <div class="d-flex justify-end mt-1">
                  <v-icon
                      v-if="competitorRound.isCurrent && selectedView === 'live'"
                      color="green"
                      small
                  >
                    mdi-play-circle
                  </v-icon>
                  <v-icon
                      v-else-if="competitorRound.score !== null"
                      color="grey"
                      small
                  >
                    mdi-check-circle
                  </v-icon>
                  <v-icon
                      v-else
                      color="grey lighten-1"
                      small
                  >
                    mdi-clock-outline
                  </v-icon>
                </div>
              </div>
            </v-list-item-action>
          </v-list-item>

          <v-divider v-if="index < displayCompetitorRounds?.length - 1" :key="`divider-${index}`"></v-divider>
        </template>
      </v-list>
    </v-card>

    <!-- No Data Message -->
    <v-card v-else-if="!isRefreshing" elevation="1">
      <v-card-text class="text-center py-4">
        <v-icon large color="grey">mdi-information-outline</v-icon>
        <div class="text-h6 grey--text mt-2">Keine Daten verfügbar</div>
        <div class="text-caption grey--text">
          <span v-if="selectedView === 'live'">Derzeit läuft keine Runde</span>
          <span v-else>Keine Daten für {{ selectedView.toUpperCase() }} verfügbar</span>
        </div>
      </v-card-text>
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
      selectedView: 'live', // 'live', 'HERREN', 'DAMEN'

      // Live data
      currentRound: null,
      currentCompetitorRound: null,
      competitorRounds: [],

      // Latest rounds data
      latestHerrenRound: null,
      latestHerrenCompetitorRounds: [],
      latestDamenRound: null,
      latestDamenCompetitorRounds: [],

      isRefreshing: false,
      autoRefreshInterval: null,
      hasLiveRound: false,

      competitorDialog: false,
      selectedCompetitor: null,

    }
  },

  computed: {
    displayRound() {
      switch (this.selectedView) {
        case 'live':
          return this.currentRound;
        case 'HERREN':
          return this.latestHerrenRound;
        case 'DAMEN':
          return this.latestDamenRound;
        default:
          return null;
      }
    },

    displayCompetitorRounds() {
      switch (this.selectedView) {
        case 'live':
          return this.competitorRounds;
        case 'HERREN':
          return this.latestHerrenCompetitorRounds;
        case 'DAMEN':
          return this.latestDamenCompetitorRounds;
        default:
          return [];
      }
    },

    completedCount() {
      if (this.selectedView === 'live') {
        return this.displayRound?.completedCompetitors || 0;
      }
      return this.displayCompetitorRounds?.filter(cr => cr.score !== null).length || 0;
    }
  },

  watch: {
    $route(to) {
      if (to.meta.type === 'live') {
        this.selectedView = 'live';
      } else if (to.meta.type === 'HERREN') {
        this.selectedView = 'HERREN';
      } else if (to.meta.type === 'DAMEN') {
        this.selectedView = 'DAMEN';
      }
    }
  },

  mounted() {
    this.initializeData();
    this.startAutoRefresh();
  },

  beforeDestroy() {
    this.stopAutoRefresh();
  },

  methods: {
    async initializeData() {
      // Try to load live data first
      await this.refreshLiveData();

      // If no live round, switch to latest HERREN
      if (!this.hasLiveRound) {
        this.selectedView = 'HERREN';
        await this.refreshLatestData('HERREN');
      }
    },

    openCompetitorDialog(competitorRound) {
      TimerApiService.getResultList(competitorRound.competitor.gender).then(response => {

        const competitor = response.data.find(cr => cr.id === competitorRound.competitor.id);
        if (competitor) {
          this.selectedCompetitor = competitor
          this.competitorDialog = true
        }

      })
    },

    async onViewChange() {
      this.isRefreshing = true;

      try {
        if (this.selectedView === 'live') {
          await this.refreshLiveData();
        } else {
          await this.refreshLatestData(this.selectedView);
        }
      } catch (error) {
        console.error('Fehler beim Wechseln der Ansicht:', error);
      } finally {
        this.isRefreshing = false;
      }
    },

    getAvatarColor(player) {
      if (player.isCurrent && this.selectedView === 'live') return 'green';
      if (player.score !== null) return 'grey';
      return 'primary';
    },

    getScoreColor(player) {
      if (player.isCurrent && this.selectedView === 'live') return 'green';
      if (player.score !== null) return 'grey';
      return 'grey lighten-2';
    },

    getScoreTextColor(player) {
      if ((player.isCurrent && this.selectedView === 'live') || player.score !== null) return 'white';
      return 'grey darken-2';
    },

    async refreshLiveData() {
      try {
        const currentRound = (await TimerApiService.getCurrentRound()).data;

        if (currentRound) {
          this.hasLiveRound = true;
          const currentCompetitorRounds = (await TimerApiService.getCurrentCompetitorRounds()).data;
          const currentCompetitorRound = (await TimerApiService.getCurrentCompetitorRound()).data;

          // Sort list
          currentCompetitorRounds.sort((a, b) => a.competitor.startNumber - b.competitor.startNumber);

          // Set position and isCurrent flag
          currentCompetitorRounds.forEach((round, index) => {
            round.position = index + 1;
            round.isCurrent = round.id === currentCompetitorRound?.id;

            if (round.isCurrent && currentCompetitorRound) {
              currentCompetitorRound.position = index + 1;
            }
          });

          this.currentRound = currentRound;
          this.competitorRounds = currentCompetitorRounds;
          this.currentCompetitorRound = currentCompetitorRound;
        } else {
          this.hasLiveRound = false;
          this.currentRound = null;
          this.competitorRounds = [];
          this.currentCompetitorRound = null;
        }
      } catch (error) {
        this.hasLiveRound = false;
        this.currentRound = null;
        this.competitorRounds = [];
        this.currentCompetitorRound = null;
      }
    },

    async refreshLatestData(gender) {
      try {
        const latestCompetitorRounds = (await TimerApiService.getLatestCompetitorRounds(gender)).data;

        if (latestCompetitorRounds && latestCompetitorRounds.length > 0) {
          // Sort by start number
          latestCompetitorRounds.sort((a, b) => a.competitor.startNumber - b.competitor.startNumber);

          // Create a mock round object from the competitor rounds data
          const mockRound = {
            gender: gender,
            numberOfCompetitors: latestCompetitorRounds.length,
            completedCompetitors: latestCompetitorRounds.filter(cr => cr.score !== null).length,
            maxHolds: latestCompetitorRounds[0]?.maxHolds || null,
            roundNumber: latestCompetitorRounds[0]?.roundNumber || 1
          };

          if (gender === 'HERREN') {
            this.latestHerrenRound = mockRound;
            this.latestHerrenCompetitorRounds = latestCompetitorRounds;
          } else {
            this.latestDamenRound = mockRound;
            this.latestDamenCompetitorRounds = latestCompetitorRounds;
          }
        }
      } catch (error) {
        console.error(`Fehler beim Laden der ${gender} Daten:`, error);
        if (gender === 'HERREN') {
          this.latestHerrenRound = null;
          this.latestHerrenCompetitorRounds = [];
        } else {
          this.latestDamenRound = null;
          this.latestDamenCompetitorRounds = [];
        }
      }
    },

    async refreshData() {
      this.isRefreshing = true;

      try {
        if (this.selectedView === 'live') {
          await this.refreshLiveData();
        } else {
          await this.refreshLatestData(this.selectedView);
        }
      } catch (error) {
        // Error handling is done in the specific refresh methods
      } finally {
        this.isRefreshing = false;
      }
    },

    startAutoRefresh() {
      this.autoRefreshInterval = setInterval(() => {
        this.refreshData();
      }, 5000);
    },

    stopAutoRefresh() {
      if (this.autoRefreshInterval) {
        clearInterval(this.autoRefreshInterval);
        this.autoRefreshInterval = null;
      }
    }
  }
}
</script>

<style scoped>
.current-player-card {
  border: 2px solid #4caf50;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(76, 175, 80, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(76, 175, 80, 0);
  }
}

.current-player-item {
  background-color: rgba(76, 175, 80, 0.1);
  border-left: 4px solid #4caf50;
}

.completed-player {
  background-color: rgba(158, 158, 158, 0.05);
}

.v-list-item {
  min-height: 64px;
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