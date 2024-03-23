<template>


  <v-footer padless>

    <v-col>
      <v-select
          v-model="selectedRoundIdLocal"
          :items="rounds"
          item-text="roundName"
          item-value="id"
          label="Runde"
      ></v-select>
    </v-col>


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

import axios from "axios";
import {Properties} from "@/config";

export default {

  props: {
    selectedRoundId: {
      type: String
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
      axios.get(Properties.API_IP + '/round/getRounds')
          .then(response => {

            this.rounds = response.data

            for (const element of this.rounds) {
              element.roundName = element.gender + " " + element.roundNumber;
            }

            this.selectedRoundIdLocal = this.rounds[0].id;

          })
          .catch(error => {
            console.error(error);
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

      axios.get(Properties.API_IP + '/print/' + type + '?id=' + this.selectedRoundIdLocal, {responseType: 'blob'})
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', 'startList.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(error => {
            console.error(error);
          });
    }

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