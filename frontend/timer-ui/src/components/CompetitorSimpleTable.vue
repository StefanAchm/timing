<template>

  <v-container>

    <v-data-table
        v-model="selectedCompetitorRoundLocal"
        :headers="headers"
        :items="competitorRounds"
        :single-select="true"
        item-key="competitor.startNumber"
        show-select
        class="elevation-1"
        sort-by="competitor.startNumber"
        hide-default-footer
    >

      <template v-slot:top>

        <v-toolbar flat>

          <v-toolbar-title>Teilnehmer</v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>


        </v-toolbar>

      </template>

      <template v-slot:[`item.competitorInfo`]="{ item }">
        {{ item.competitor.firstName }} {{ item.competitor.lastName }}
      </template>

      <template v-slot:[`item.competitorPoints`]="{ item }">
        {{ item.score }}
      </template>

    </v-data-table>

  </v-container>

</template>

<script>

import {Properties} from "@/config";
import axios from "axios";

export default {

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
      {text: 'Startnummer', value: 'competitor.startNumber', sortable: false},
      {text: 'TeilnehmerInn', value: 'competitorInfo', sortable: false},
      {text: 'Punkte', value: 'competitorPoints', sortable: false},
    ],

  }),

  computed: {
    selectedCompetitorRoundLocal: {
      get() {
        return [this.selectedCompetitorRound];
      },
      set(value) {
        console.log(value[0]);
        this.$emit('update:selectedCompetitorRound', value[0]);

      }
    }

  },

  watch: {
    roundId: function (newVal) {
      this.loadCompetitors(newVal);
    },
  },

  created() {
    this.loadCompetitors(this.roundId);
  },

  methods: {

    loadCompetitors(id) {

      if(!id) {
        return;
      }

      axios.get(Properties.API_IP + '/competitor-round/getCompetitorRounds' + '?roundId=' + id)
          .then(response => {
            console.log(response.data);
            this.competitorRounds = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },

  },



}
</script>