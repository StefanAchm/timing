<template>

  <v-container>

    <v-data-table
        :headers="headers"
        :items="competitorRounds"
        item-key="competitor.startNumber"
        class="elevation-1"
        hide-default-footer
        fixed-header
        height="500px"
        :items-per-page="competitorRounds.length"
    >

      <template v-slot:top>

        <v-toolbar flat>

          <v-toolbar-title>Teilnehmer</v-toolbar-title>

          <v-divider class="mx-4" inset vertical></v-divider>

          <v-spacer></v-spacer>

          <v-icon @click="add">mdi-plus</v-icon>

          <CompetitorDialog
              :dialog.sync="dialogVisible"
              :competitor="{}"
              :full-edit="true"
          />



        </v-toolbar>

      </template>

      <template v-slot:item="{ item }">
        <tr :class="getRowStyle(item)">

          <td>{{ item.competitor.startNumber }}</td>
          <td>{{ item.competitorNumber}}</td>
          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td> {{ item.score }} </td>
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
                small class="mr-2"
                @click="chooseItem(item)">
              mdi-open-in-new
            </v-icon>


          </td>

        </tr>

      </template>

    </v-data-table>

  </v-container>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";
import CompetitorDialog from "@/components/CompetitorDialog.vue";

export default {
  components: {CompetitorDialog},

  props: {
    roundId: {
      type: String
    },
    selectedCompetitorRound: {
      type: Object
    }
  },

  data: () => ({

    competitorRounds: [],
    headers: [
      {text: '#', value: 'competitor.startNumber', sortable: false},
      {text: '+/-', value: 'competitorNumber', sortable: false},
      {text: 'TeilnehmerInn', value: 'competitorInfo', sortable: false},
      {text: 'Punkte', value: 'competitorPoints', sortable: false},
      {text: '', value: 'action', sortable: false},
    ],

    dialogVisible: false,

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


  },

  watch: {
    roundId: function (newVal) {
      this.loadCompetitors(newVal);
      this.selectedCompetitorRoundLocal = null;
    },



    // TODO: need a sorted list of competitorRounds!
    // selectedCompetitorRoundLocal: {
    //   handler(newVal) {
    //     console.log('selectedCompetitorRoundLocal', newVal);
    //
    //     if(newVal.competitorRoundStatus === 'COMPLETED') {
    //       let index = this.competitorRounds.indexOf(newVal);
    //       if(index < this.competitorRounds.length - 1) {
    //         this.selectedCompetitorRoundLocal = this.competitorRounds[index + 1];
    //       }
    //     }
    //   }, deep: true
    //
    // }

  },

  created() {
    this.loadCompetitors(this.roundId);
  },

  methods: {

    add() {
      this.dialogVisible = true;
    },

    getRowStyle(item) {
      if(item.competitorRoundStatus === 'COMPLETED') {
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
        if(i < index) {
          cr.competitorNumber = '-' + (index - i);
        } else if(i === index) {
          cr.competitorNumber = '0';
        } else {
          cr.competitorNumber = '+' + (i - index);
        }
      });
    },

    loadCompetitors(id) {

      if(!id) {
        return;
      }

      TimerApiService.getCompetitorRounds(id)
          .then(response => {
            this.competitorRounds = response.data;
            // Sort list

            this.competitorRounds.sort((a, b) => {
              return a.competitor.startNumber - b.competitor.startNumber;
            });

          });
    },

  },



}
</script>