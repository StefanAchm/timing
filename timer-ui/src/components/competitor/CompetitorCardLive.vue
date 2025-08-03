<template>
  <v-dialog
      v-model="dialogLocal"
      @input="$emit('input', $event)"
      max-width="800px"
      scrollable
  >
    <v-card v-if="competitor">
      <!-- Dialog Header -->
      <v-card-title class="primary white--text py-4">
        <div class="d-flex align-center">
          <!-- Rank Badge -->
          <v-avatar
              size="48"
              :color="getRankColor(competitor.rank)"
              class="mr-4"
          >
            <v-icon
                v-if="competitor.rank <= 3"
                color="white"
            >
              {{ getRankIcon(competitor.rank) }}
            </v-icon>
            <span
                v-else
                class="white--text text-h6"
            >
              {{ competitor.rank }}
            </span>
          </v-avatar>

          <div>
            <div class="text-h5 font-weight-bold">
              {{ competitor.firstName }}
            </div>
            <div class="text-h5 font-weight-bold">
              {{ competitor.lastName }}
            </div>
            <div class="text-subtitle-1 white--text">
              Platz {{ competitor.rank }} - {{ competitor.points.toFixed(4) }} Punkte
            </div>
          </div>
        </div>

      </v-card-title>

      <v-card-text class="pa-0">
        <!-- Competitor Info Section -->
        <div class="pa-4">
          <v-row>
            <v-col cols="12" md="6">
              <v-card outlined class="mb-4">
                <v-card-subtitle class="pb-2">
                  <v-icon small class="mr-2">mdi-account-details</v-icon>
                  Teilnehmer-Informationen
                </v-card-subtitle>

                <v-card-text class="pt-0">
                  <v-divider></v-divider>
                  <div class="mb-2 mt-2" v-if="competitor.city">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-map-marker</v-icon>
                    <span class="text-body-2">{{ competitor.city }}</span>
                  </div>
                  <div class="mb-0" v-if="competitor.club">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-account-group</v-icon>
                    <span class="text-body-2">{{ competitor.club }}</span>
                  </div>
                </v-card-text>
              </v-card>
            </v-col>

            <v-col cols="12" md="6">
              <v-card outlined class="mb-4">
                <v-card-subtitle class="pb-2">
                  <v-icon small class="mr-2">mdi-trophy</v-icon>
                  Aktuelle Leistung
                </v-card-subtitle>

                <v-card-text class="pt-0">
                  <v-divider></v-divider>
                  <div class="mb-2 mt-2">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-numeric</v-icon>
                    <span class="text-body-2">Letzte Runde: {{ competitor.lastRound }}</span>
                  </div>
                  <div class="mb-2">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-hand-back-right</v-icon>
                    <span class="text-body-2">Griff {{ competitor.holdNumber }} {{ competitor.holdType }}</span>
                  </div>
                  <div class="mb-2">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-counter</v-icon>
                    <span class="text-body-2">{{ competitor.tryNumber }} Versuch{{ competitor.tryNumber !== 1 ? 'e' : '' }}</span>
                  </div>
                  <div class="mb-0">
                    <v-icon small color="grey" class="mr-2 ml-2">mdi-chart-line</v-icon>
                    <span class="text-body-2 font-weight-bold">Gesamtpunkte: {{ competitor.points.toFixed(4) }}</span>
                  </div>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </div>

        <!-- Round Details Section -->
        <v-divider></v-divider>
        <div class="pa-4">
          <div class="text-h6 mb-4 d-flex align-center">
            <v-icon class="mr-2">mdi-format-list-numbered</v-icon>
            Rundendetails
          </div>

          <v-row>
            <v-col
                cols="12"
                md="6"
                v-for="(round, index) in competitorRoundScoresSorted()"
                :key="index"
            >
              <v-card
                  outlined
                  :class="getRoundCardClass(round.lastRound)"
                  class="round-card"
              >
                <v-card-title class="py-3">
                  <v-icon
                      :color="getRoundIconColor(round.lastRound)"
                      class="mr-2"
                  >
                    mdi-numeric-{{ round.lastRound }}-circle
                  </v-icon>
                  <span class="text-h6">Runde {{ round.lastRound }}</span>
                  <v-spacer></v-spacer>
                  <!-- TODO: make a chip to indicate if top or not -->
<!--                  <v-chip-->
<!--                      small-->
<!--                      :color="getHoldTypeColor(round.holdType)"-->
<!--                      text-color="white"-->
<!--                  >-->

<!--                  </v-chip>-->
                </v-card-title>

                <v-card-text>
                  <div class="round-details">
                    <div class="d-flex justify-space-between align-center mb-2">
                      <div class="text-body-2 grey--text">
                        <v-icon small color="grey" class="mr-1">mdi-hand-back-right</v-icon>
                        Griff
                      </div>
                      <div class="text-body-1 font-weight-medium">
                        {{ round.holdNumber }}  {{ round.holdType }}
                      </div>
                    </div>

                    <div class="d-flex justify-space-between align-center mb-2">
                      <div class="text-body-2 grey--text">
                        <v-icon small color="grey" class="mr-1">mdi-counter</v-icon>
                        Versuche
                      </div>
                      <div class="text-body-1 font-weight-medium">
                        {{ round.tryNumber }}
                      </div>
                    </div>

                    <v-divider class="my-3"></v-divider>

                    <div class="d-flex justify-space-between align-center">
                      <div class="text-body-1 font-weight-medium">
                        <v-icon small class="mr-1">mdi-star</v-icon>
                        Punkte
                      </div>
                      <div class="text-h6 font-weight-bold primary--text">
                        {{ round.points.toFixed(4) }}
                      </div>
                    </div>
                  </div>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </div>
      </v-card-text>

      <!-- Dialog Actions -->
      <v-divider></v-divider>
      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn
            color="primary"
            @click="close()"
        >
          Schließen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'CompetitorDialog',

  props: {
    dialog: {
      type: Boolean,
      default: false
    },
    competitor: {
      type: Object,
      default: null
    }
  },

  computed: {
    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },
  },

  methods: {
    getRankColor(rank) {
      if (rank === 1) return 'amber darken-2'   // Gold
      if (rank === 2) return 'blue-grey lighten-1' // Silver
      if (rank === 3) return 'brown lighten-1'  // Bronze
      return 'grey' // Default for others
    },

    getRankIcon(rank) {
      if (rank === 1) return 'mdi-trophy'
      if (rank === 2) return 'mdi-medal'
      if (rank === 3) return 'mdi-medal'
      return ''
    },

    getRoundCardClass(roundNumber) {
      return `round-${roundNumber}`
    },

    getRoundIconColor(roundNumber) {
      const colors = ['primary', 'success', 'warning', 'error', 'info']
      return colors[(roundNumber - 1) % colors.length] || 'grey'
    },

    close() {
      this.dialogLocal = false
      this.$emit('close')
    },

    competitorRoundScoresSorted() {
      return [...this.competitor.competitorRoundScores].sort((a, b) => a.lastRound - b.lastRound)
    },

    getHoldTypeColor(holdType) {
      switch (holdType.toLowerCase()) {
        case 'gehalten':
          return 'success'
        case 'berührt':
          return 'warning'
        case 'nicht berührt':
        case 'nicht geschafft':
          return 'error'
        default:
          return 'grey'
      }
    }
  }
}
</script>

<style scoped>
.round-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.round-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.round-1 {
  border-left: 4px solid #1976d2;
}

.round-2 {
  border-left: 4px solid #4caf50;
}

.round-3 {
  border-left: 4px solid #ff9800;
}

.round-4 {
  border-left: 4px solid #f44336;
}

.round-5 {
  border-left: 4px solid #2196f3;
}

.round-details {
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  padding: 12px;
}
</style>