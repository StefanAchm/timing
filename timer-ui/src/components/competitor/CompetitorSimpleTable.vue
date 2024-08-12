<template>

  <v-container>

    <v-data-table
        :headers="headers"
        :items="competitorRounds"
        item-key="competitor.startNumber"
        class="elevation-1"
        hide-default-footer
        fixed-header
        height="650px"
        :items-per-page="competitorRounds.length"
        v-if="competitorRounds.length > 0"
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
                color="primary"
                class="mb-2"
                @click="selectedCompetitorRoundLocal = null"
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

      <template v-slot:item="{ item }">
        <tr :class="getRowStyle(item)">

          <td>{{ item.competitor.startNumber }}</td>
          <td>{{ item.competitorNumber }}</td>
          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td> {{ item.score }}</td>
          <td>

            <v-progress-circular
                v-if="item === selectedCompetitorRoundLocal"
                indeterminate
                color="primary"
                size="20"
                class="mr-2"
            ></v-progress-circular>

            <v-icon
                v-else
                class="mr-2"
                @click="chooseItem(item)">
              mdi-play-circle-outline
            </v-icon>


          </td>

        </tr>

      </template>

    </v-data-table>

    <v-card v-else>

      <v-card-title>
        Bitte eine Runde auswählen
      </v-card-title>

    </v-card>

  </v-container>

</template>

<script>

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

  methods: {

    getRowStyle(item) {
      if (item.competitorRoundStatus === 'COMPLETED') {
        return 'grey lighten-2 grey--text';
      } else {
        return '';
      }
    },

    chooseItem(item) {
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
    },

  },


}
</script>