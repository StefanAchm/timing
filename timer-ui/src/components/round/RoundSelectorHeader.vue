<template>


  <v-footer>


    <v-select
        v-model="selectedRoundIdLocal"
        :items="rounds"
        item-text="roundName"
        item-value="id"
        label="Runde"
    ></v-select>

    <RoundDialog
        :dialog.sync="addDialog"
        :round="{}"
    />

    <v-icon
        @click="addDialog = true"
    >
      mdi-plus
    </v-icon>

    <!--    <v-icon-->
    <!--        @click="addDialog = true"-->
    <!--    >-->
    <!--      mdi-pencil-->
    <!--    </v-icon>-->


    <v-spacer></v-spacer>

    <v-btn color="blue darken-1" text @click="downloadStartList()">
      Startliste herunterladen
    </v-btn>

    <v-btn color="blue darken-1" text @click="downloadResultList()">
      Ergebnisliste herunterladen
    </v-btn>


  </v-footer>


</template>

<script>

import RoundDialog from "@/components/round/RoundDialog.vue";
import TimerApiService from "@/plugins/timer-api";

export default {

  components: {RoundDialog},

  props: {
    selectedRoundId: {
      type: String
    }
  },

  data: () => ({

    rounds: [],
    addDialog: false

  }),

  created() {
    this.loadRounds();
  },

  methods: {
    loadRounds() {
      TimerApiService.getRounds()
          .then(response => {

            this.rounds = response.data

            for (const element of this.rounds) {
              element.roundName = element.gender + " " + element.roundNumber;
            }

            if (this.rounds.length > 0) {
              this.selectedRoundIdLocal = this.rounds[0].id;
            }

          })

    },

    roundText(id) {

      let round = this.rounds.find(round => round.id === id)

      return round.roundNumber + "." +
          "Runde der " + round.gender +
          " mit " + round.numberOfCompetitors + " TeilnehmerInnen " +
          "(maximal " + round.maxHolds + " Griffe)";

    },


    downloadStartList() {

      let round = this.rounds.find(round => round.id === this.selectedRoundIdLocal)

      let filename = 'startList-' + round.gender + '-' + round.roundNumber + '.pdf';

      TimerApiService.printStartList(this.selectedRoundIdLocal)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          });
    },

    downloadResultList() {

      let round = this.rounds.find(round => round.id === this.selectedRoundIdLocal)

      let filename = 'resultList-' + round.gender + '.pdf';

      TimerApiService.printResultList(round.gender)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          });

    },

  },

  computed: {
    selectedRoundIdLocal: {
      get() {
        return this.selectedRoundId
      },
      set(value) {
        this.$emit('update:selectedRoundId', value)
      }
    }
  }

}

</script>