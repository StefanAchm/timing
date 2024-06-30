<template>

  <v-data-table
      :headers="headers"
      :items="results"
      class="elevation-1"
  >

    <template v-slot:top>

      <v-toolbar flat>
        <v-toolbar-title>Ergebnisse</v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn
            @click="updateRanks"
            color="primary"
            text>
          Update Ranks
        </v-btn>

        <v-btn
          @click="print"
          color="primary">
          Drucken
        </v-btn>

      </v-toolbar>

    </template>

    <template v-slot:[`item.competitor`]="{ item }">

      <span>
        {{ item.competitor.firstName }}
        {{ item.competitor.lastName }}
      </span>

    </template>

    <template v-slot:[`item.score`]="{ item }">

      <v-text-field
          v-model.number="item.score"
          type="number">

      </v-text-field>

    </template>

  </v-data-table>

</template>

<script>

import TimerApi from "@/plugins/timer-api";
import TimerApiService from "@/plugins/timer-api";

export default {

  props: {
    gender: {
      type: String,
    }
  },

  data: () => ({

    headers: [
      { text: 'Rank', value: 'rank' },
      { text: 'TeilnehmerInn', value: 'competitor', sortable: false },
      { text: 'Score', value: 'score', sortable: false },
    ],

    results: []

  }),

  created() {
    this.loadResults();
  },

  methods: {

    loadResults() {
      TimerApi.previewResultList(this.gender)
        .then(response => {
          this.results = response.data;
          console.log(this.results)
        })
        .catch(() => {});
    },

    updateRanks() {
      this.results.sort((a, b) => b.score - a.score);
      this.results.forEach((result, index) => {
        result.rank = index + 1;
      });
    },

    print() {


      let filename = 'resultList-' + this.gender + '.pdf';

      console.log(filename)

      TimerApiService.resultListFromData(this.gender, this.results)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(() => {
          });
    }

  }

}

</script>