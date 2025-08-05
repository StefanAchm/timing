<template>

  <v-container>

    <v-card v-if="!competitorRounds">

      <v-card-title>
        Bitte eine Runde auswählen
      </v-card-title>

    </v-card>

    <v-card v-else-if="competitorRounds.length === 0">

      <v-card-title>
        Keine TeilnehmerInnen in dieser Runde
      </v-card-title>
    </v-card>

    <v-data-table
        ref="competitorSimpleTable"
        :headers="headers"
        :items="competitorRounds"
        item-key="competitor.startNumber"
        class="elevation-1"
        hide-default-footer
        fixed-header
        height="435px"
        :items-per-page="competitorRounds.length"
        v-else
    >

      <template v-slot:top>

        <v-card
            :elevation="0"
        >

          <v-card-title>

            Runde {{ competitorRounds[0]?.roundNumber }}
            der {{ competitorRounds[0]?.competitor.gender }}

            <v-spacer></v-spacer>


            <v-btn
                :disabled="selectedCompetitorRoundLocal === null"
                color="warning"
                class="mb-2 black--text"
                @click="stopSelectedRound()"
            >Stopp
            </v-btn>

          </v-card-title>

          <v-card-subtitle>

            <v-row>

              <v-col>

                TeilnehmerInnen: {{ competitorRounds.length }} <br>
                Noch nicht gestartet: {{ competitorRoundsCompleted.length }} <br>
                Anzahl Griffe: {{ selectedRound?.maxHolds }} <br>

              </v-col>

            </v-row>

          </v-card-subtitle>

        </v-card>

      </template>

      <template v-slot:item="{ item, index }">
        <tr :class="getRowStyle(item)">

          <td> {{ index + 1 }} ({{ item.competitor.startNumber }})</td>
          <td>{{ item.competitorNumber }}</td>
          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td> {{ item.score }}</td>
          <td>

            <v-progress-circular
                v-if="item === selectedCompetitorRoundLocal"
                indeterminate
                color="primary"
                size="23"
                class="mr-2"
            ></v-progress-circular>

            <v-icon
                v-else
                class="mr-2"
                @click="chooseItem(item)">
              mdi-play-circle-outline
            </v-icon>

            <!-- add a tooltip, with all the information about the competitor -->
            <v-tooltip bottom color="primary">
              <template v-slot:activator="{ on, attrs }">
                <v-icon v-bind="attrs" v-on="on">mdi-information</v-icon>
              </template>
              <span>
                Wohnort: {{ item.competitor.city }}
                <br>
                Verein: {{ item.competitor.club }}
                <br>
                Geburtsdatum: {{ item.competitor.dateOfBirth }}
              </span>

            </v-tooltip>

          </td>

        </tr>

      </template>

    </v-data-table>

  </v-container>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

export default {

  props: {
    selectedCompetitorRound: {
      type: Object
    },
    competitorRounds: {
      type: Array
    },
    selectedRound: {
      type: Object
    }
  },

  data: () => ({

    headers: [
      {text: '#', value: 'competitor.startNumber', sortable: false},
      {text: '+/-', value: 'competitorNumber', sortable: false},
      {text: 'TeilnehmerInn', value: 'competitorInfo', sortable: false},
      {text: 'Punkte', value: 'competitorPoints', sortable: false},
      {text: '', value: 'action', sortable: false},
    ],

  }),

  computed: {

    selectedCompetitorRoundLocal: {
      get() {
        return this.selectedCompetitorRound;
      },
      set(value) {
        this.$emit('update:selectedCompetitorRound', value);
      }
    },

    competitorRoundsCompleted() {
      return this.competitorRounds.filter(competitorRound => competitorRound.competitorRoundStatus === 'CREATED');
    }

  },

  created() {

  },

  watch: {
    selectedCompetitorRoundLocal: function (newVal) {
      this.chooseItem(newVal)

      if(newVal === null) {
        TimerApiService.updateCompetition(null, null);
        return;
      }

      TimerApiService.updateCompetition(this.selectedRound.id, this.selectedCompetitorRoundLocal.id)

    }
  },

  methods: {

    async stopSelectedRound() {
      this.selectedCompetitorRoundLocal = null
    },

    getRowStyle(item) {
      if (item === this.selectedCompetitorRoundLocal) {
        return 'warning';
      } else if (item.competitorRoundStatus === 'COMPLETED') {
        return 'neutral2 grey--text';
      } else {
        return '';
      }
    },

    async chooseItem(item) {

      this.selectedCompetitorRoundLocal = item;

      // Update +/- on all items
      let index = this.competitorRounds.indexOf(item);
      this.competitorRounds.forEach((cr, i) => {
        if (i < index) {
          cr.competitorNumber = '-' + (index - i);
        } else if (i === index) {
          cr.competitorNumber = '0';
        } else {
          cr.competitorNumber = '+' + (i - index);
        }
      });
      this.scrollToSelectedRow();
    },

    scrollToSelectedRow() {
      this.$nextTick(() => {
        const selectedRow = this.$refs.competitorSimpleTable.$el.querySelector('.secondary');
        if (selectedRow) {
          selectedRow.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
      });
    },

  },


}
</script>