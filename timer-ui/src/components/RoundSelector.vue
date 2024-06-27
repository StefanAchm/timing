<template>

  <v-card>

<!--    <v-card-title>-->
<!--      <h2>Runde auswählen</h2>-->
<!--    </v-card-title>-->

    <v-card-text>
      <v-row>
        <v-col>
          <v-select
              v-model="selectedRoundLocalId"
              :items="rounds"
              item-text="roundName"
              item-value="id"
              label="Runde"
          ></v-select>
        </v-col>
      </v-row>
    </v-card-text>

    <v-card-text >
      {{ roundText(selectedRoundLocalId) }}
    </v-card-text>

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn color="blue darken-1" text @click="downloadStartList()">
        Startliste herunterladen
      </v-btn>

      <v-btn color="blue darken-1" text @click="downloadResultList()">
        Ergebnisliste herunterladen
      </v-btn>

    </v-card-actions>

  </v-card>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

export default {

  props: {
    selectedRound: {
      type: Object
    }
  },

  data: () => ({

    rounds: []

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

            this.selectedRoundLocalId = this.rounds[0].id;

          });

      },

    roundText(id) {

      let round = this.rounds.find(round => round.id === id)

      return round.roundNumber + "." +
          "Runde der " + round.gender +
          " mit " + round.numberOfCompetitors + " TeilnehmerInnen " +
          "(maximal " + round.maxHolds + " Griffe)";

    },

    downloadStartList() {

      this.download('startList')

    },

    downloadResultList() {
      this.download('resultList')
    },

    download(type) {

      TimerApiService.print(type, this.selectedRoundLocalId)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', 'startList.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
          });
    }

  },

  computed: {
    selectedRoundLocalId: {
      get() {
        return this.selectedRound
      },
      set(value) {
        this.$emit('update:selectedRound', value)
      }
    }
  }

}

</script>