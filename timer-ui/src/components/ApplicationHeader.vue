<template>
 <!-- Create a new component, which is on top of every page and allows to do the following: -->
  <!-- Add: Competitors or Rounds -->
  <!-- Print: different lists -->

  <v-app-bar app color="secondary">

    <CompetitorDialog
        :dialog.sync="competitorDialog"
        @dialog-closed="init()"
        :full-edit="true"/>

    <RoundDialog
        :dialog.sync="roundDialog"
        :rounds="rounds"
        :round="round"
        @dialog-closed="init()"
    />

    <PrintDialog :dialog.sync="printDialog"></PrintDialog>


    <v-toolbar-title></v-toolbar-title>

    <v-menu offset-y>
      <template v-slot:activator="{ on }">
        <v-btn text v-on="on">Hinzufügen</v-btn>
      </template>

      <v-list>
        <v-list-item @click="competitorDialog = true">Teilnehmer hinzufügen</v-list-item>
        <v-list-item @click="roundDialog = true">Runde hinzufügen</v-list-item>
      </v-list>
    </v-menu>


    <v-spacer></v-spacer>
    <v-btn text @click="printDialog = true">Drucken</v-btn>

  </v-app-bar>

</template>

<script>

import PrintDialog from "@/components/PrintDialog.vue";
import CompetitorDialog from "@/components/competitor/CompetitorDialog.vue";
import RoundDialog from "@/components/round/RoundDialog.vue";
import TimerApiService from "@/plugins/timer-api";

export default {

  components: {RoundDialog, CompetitorDialog, PrintDialog},

  data: () => ({

    printDialog: false,
    competitorDialog: false,
    roundDialog: false,

    rounds: [],

    round: {
      score: {
        holdType: null,
        holdNumber: null,
        tryNumber: null
      }
    },

  }),

  created() {
    this.init();
  },

  methods: {

    init() {
      this.loadRounds();
    },

    loadRounds() {
      TimerApiService.getRounds()
          .then(response => {

            this.rounds = response.data

            for (const element of this.rounds) {
              element.roundName = element.roundNumber + ' - ' + element.gender + '';
            }

            // sort by roundNumber

            this.rounds.sort((a, b) => {
              return a.roundNumber - b.roundNumber;
            });

            if (this.rounds.length > 0) {
              this.selectedRoundId = this.rounds[0].id;
            }

            console.log(this.rounds)

          })
          .catch(() => {
          });

    },

  }

};
</script>