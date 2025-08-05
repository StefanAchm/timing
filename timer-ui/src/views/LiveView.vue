<template>
  <v-container fluid class="pa-2">
    <!-- Header Card -->
    <v-card class="mb-3" elevation="2">
      <v-card-title class="text-h7 primary white--text py-3">
        <v-icon left color="white">mdi-trophy</v-icon>
        Runde {{ currentRound?.roundNumber }} der {{ currentRound?.gender }}
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

      <v-card-text class="pb-2 pt-2">
        <v-row no-gutters>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Teilnehmer</div>
            <div class="text-h6">{{ currentRound?.numberOfCompetitors }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Noch ausstehend</div>
            <div class="text-h6">{{ currentRound?.numberOfCompetitors - currentRound?.completedCompetitors }}</div>
          </v-col>
          <v-col cols="4" class="text-center">
            <div class="text-caption grey--text">Griffe</div>
            <div class="text-h6">{{ currentRound?.maxHolds }}</div>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- Current Player Card -->
    <v-card
        v-if="currentCompetitorRound"
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
    <v-card elevation="2" v-if="competitorRounds?.length > 0">
      <v-card-title class="text-h6 py-2">
        <v-icon left>mdi-format-list-numbegreen</v-icon>
        Startliste
      </v-card-title>

      <v-list dense>
        <template v-for="(competitorRound, index) in competitorRounds">
          <v-list-item
              :key="index"
              :class="{
              'current-player-item': competitorRound.isCurrent,
              'completed-player': competitorRound.score !== null && !competitorRound.isCurrent
            }"
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
                  :class="{ 'font-weight-bold': competitorRound.isCurrent }"
              >
                {{ competitorRound.competitor.firstName }} {{ competitorRound.competitor.lastName }}
              </v-list-item-title>
              <!--              <v-list-item-subtitle v-if="competitorRound.handicap !== 0">-->
              <!--                {{ competitorRound.handicap > 0 ? '+' : '' }}{{ competitorRound.handicap }}-->
              <!--              </v-list-item-subtitle>-->
            </v-list-item-content>

            <!-- Score and Status -->
            <v-list-item-action>
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
                      v-if="competitorRound.isCurrent"
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

          <v-divider v-if="index < competitorRounds?.length - 1" :key="`divider-${index}`"></v-divider>
        </template>
      </v-list>
    </v-card>

  </v-container>
</template>

<script>

import TimerApiService from "@/plugins/timer-api";

export default {

  data() {
    return {

      currentRound: null,
      currentCompetitorRound: null,
      competitorRounds: [],
      isRefreshing: false,
      autoRefreshInterval: null,

    }
  },

  computed: {},

  mounted() {
    this.refreshData()
    // Auto-refresh every 30 seconds
    this.startAutoRefresh()
  },

  beforeDestroy() {
    this.stopAutoRefresh()
  },

  methods: {

    getAvatarColor(player) {
      if (player.isCurrent) return 'green'
      if (player.score !== null) return 'grey'
      return 'primary'
    },

    getScoreColor(player) {
      if (player.isCurrent) return 'green'
      if (player.score !== null) return 'grey'
      return 'grey lighten-2'
    },

    getScoreTextColor(player) {
      if (player.isCurrent || player.score !== null) return 'white'
      return 'grey darken-2'
    },

    async refreshData() {
      this.isRefreshing = true

      try {

        const currentRound = (await TimerApiService.getCurrentRound()).data;

        const currentCompetitorRounds = (await TimerApiService.getCurrentCompetitorRounds()).data;

        // Sort list
        currentCompetitorRounds.sort((a, b) => a.competitor.startNumber - b.competitor.startNumber);

        const currentCompetitorRound = (await TimerApiService.getCurrentCompetitorRound()).data;

        // Go through the competitor rounds, set the position and the isCurrent flag
        currentCompetitorRounds.forEach((round, index) => {
          round.position = index + 1;
          round.isCurrent = round.id === currentCompetitorRound?.id;

          if (round.isCurrent) {
            currentCompetitorRound.position = index + 1;
          }
        });

        this.currentRound = currentRound;
        this.competitorRounds = currentCompetitorRounds;
        this.currentCompetitorRound = currentCompetitorRound;

      } catch (error) {
        // console.error('Fehler beim Aktualisieren:', error)
      } finally {
        this.isRefreshing = false
      }
    },

    startAutoRefresh() {
      this.autoRefreshInterval = setInterval(() => {
        this.refreshData()
      }, 5000) // 30 Sekunden
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
.current-player-card {
  border: 2px solid var(--v-primary-green);
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
  border-left: 4px solid var(--v-primary-green);
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