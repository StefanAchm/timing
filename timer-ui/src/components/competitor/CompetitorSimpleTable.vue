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

          <v-toolbar-title>

            ({{ competitorRoundsCompleted.length }}/{{ competitorRounds.length }}) Teilnehmer
            in Runde {{ competitorRounds[0]?.roundNumber }}
            der {{ competitorRounds[0]?.competitor.gender }}

          </v-toolbar-title>

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


import CompetitorDialog from "@/components/competitor/CompetitorDialog.vue";

export default {
  components: {CompetitorDialog},

  props: {
    selectedCompetitorRound: {
      type: Object
    },
    competitorRounds: {
      type: Array
    }
  },

  data: () => ({

    // competitorRounds: [],
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

    competitorRoundsCompleted() {
      return this.competitorRounds.filter(competitorRound => competitorRound.competitorRoundStatus === 'CREATED');
    }

  },

  created() {

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

  },



}
</script>