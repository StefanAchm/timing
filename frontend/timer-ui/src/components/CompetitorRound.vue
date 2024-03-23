<template>

  <v-container>

    <v-card>

      <v-card-title v-if="competitorRoundLocal">
        {{ competitorRoundLocal.competitor.firstName + ' ' + competitorRoundLocal.competitor.lastName }}
      </v-card-title>

      <v-card-title v-else>
        Bitte einen Teilnehmer auswählen
      </v-card-title>


      <v-card-text v-if="competitorRoundLocal">
        <v-container>
          <v-row>

            <HoldTypeSelector
                :hold-type.sync="holdTypeSelected"
                :selectDisabled="false"
            />

          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorRoundLocal.holdNumber" label="Griffnummer"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorRoundLocal.tryNumber" label="Versuch"></v-text-field>
            </v-col>
          </v-row>
        </v-container>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="blue darken-1" text @click="save">
            Speichern
          </v-btn>

        </v-card-actions>

      </v-card-text>

    </v-card>
  </v-container>

</template>

<script>

import {Properties} from "@/config";
import axios from "axios";
import HoldTypeSelector from "@/components/HoldTypeSelector.vue";

export default {

  components: {HoldTypeSelector},

  props: {
    competitorRound: {
      type: Object
    }
  },

  data: () => ({

    holdTypes: [],

    holdTypeSelected: null,

  }),

  created() {

  },

  watch: {

    holdTypeSelected(val) {
      this.competitorRoundLocal.holdType = val;
    },

  },

  methods: {

    save() {

      this.competitorRoundLocal.competitorRoundStatus = 'COMPLETED';

      axios.post(Properties.API_IP + '/competitor-round/update',
          JSON.stringify(this.competitorRoundLocal),
          {headers: {'Content-Type': 'application/json'}})
          .then(response => {

            this.competitorRoundLocal.score = response.data;
            // this.selectedCompetitorRoundIndex += 1;
          })
    },
  },

  computed: {

    competitorRoundLocal: {
      get() {
        return this.competitorRound
      },
      set(value) {
        this.$emit('update:competitorRound', value)
      }
    },

  }

}

</script>