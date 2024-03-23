<template>

  <v-container>

    <v-data-table
        :headers="headers"
        :items="competitorRounds"
        item-key="competitor.startNumber"
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

      <template v-slot:item="{ item }">
        <tr :class="getRowStyle(item)">

          <td>{{ item.competitor.startNumber }}</td>
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
      {text: '', value: 'action', sortable: false},
    ],

  }),

  computed: {
    selectedCompetitorRoundLocal: {
      get() {
        return this.selectedCompetitorRound;
      },
      set(value) {
        console.log('set selectedCompetitorRoundLocal', value);
        this.$emit('update:selectedCompetitorRound', value);
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

    getRowStyle(item) {
      if(item.competitorRoundStatus === 'COMPLETED') {
        return 'grey lighten-2 grey--text';
      } else {
        return '';
      }
    },

    chooseItem(item) {
      this.selectedCompetitorRoundLocal = item;
    },

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