<template>

  <v-container>

    <v-row>

      <v-col sm="6">

        <v-card
            max-width="450"
            class="mx-auto"
        >
          <v-toolbar
              color="cyan"
              dark
          >

          </v-toolbar>

          <v-list
              two-line
              style="max-height: 800px; overflow-y: auto"
          >


            <v-list-item-group
                v-model="selectedCompetitorRoundIndex"
                key="selectedCompetitorRound"
            >


              <v-list-item
                  :key="item.id"
                  v-for="(item) in sortedCompetitorRounds"
                  :style="{
                    backgroundColor: item.competitorRoundStatus === 'COMPLETED' ? 'lightgrey' : 'white',
                    color: item.competitorRoundStatus === 'COMPLETED' ? 'grey' : 'black'
                  }"
              >

                <v-list-item-avatar>
                  <v-chip color="secondary" v-html="item.competitor.startNumber"></v-chip>
                </v-list-item-avatar>

                <v-list-item-content>

                  <v-list-item-title>
                    {{ item.competitor.firstName }} {{ item.competitor.lastName }}
                  </v-list-item-title>

                  <v-list-item-subtitle v-if="item.competitorRoundStatus === 'COMPLETED'">
                    Griff {{item.holdNumber}} {{item.holdType}} beim {{item.tryNumber}} Versuch
                  </v-list-item-subtitle>
                  <v-list-item-subtitle v-else>
                    Keine Bewertung
                  </v-list-item-subtitle>

                </v-list-item-content>

                <v-list-item-action v-if="item.competitorRoundStatus === 'COMPLETED'" >
                  <v-chip color="primary" v-html="item.score"></v-chip>
                </v-list-item-action>

              </v-list-item>

            </v-list-item-group>

          </v-list>

        </v-card>

      </v-col>

      <v-col sm="6">

        <v-card>

          <v-card-title v-if="selectedCompetitorRound">
            {{ selectedCompetitorRound.competitor.firstName + ' ' + selectedCompetitorRound.competitor.lastName }}
          </v-card-title>

          <v-card-title v-else>
            Bitte einen Teilnehmer auswählen
          </v-card-title>


          <v-card-text v-if="selectedCompetitorRound">
            <v-container>
              <v-row>

                <HoldTypeSelector
                    :hold-type.sync="holdTypeSelected"
                    :selectDisabled="false"
                />


<!--                <v-select-->
<!--                    v-model="selectedCompetitorRound.holdType"-->
<!--                    :items="holdTypes"-->
<!--                    item-text="name"-->
<!--                    item-value="id"-->
<!--                    label="Griffart"-->

<!--                ></v-select>-->

              </v-row>
              <v-row>
                <v-col>
                  <v-text-field v-model="selectedCompetitorRound.holdNumber" label="Griffnummer"></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field v-model="selectedCompetitorRound.tryNumber" label="Versuch"></v-text-field>
                </v-col>
              </v-row>
            </v-container>

            <!-- Save the changes to the competitorRound -->

            <!-- To align the button on the right, use the v-spacer component -->
            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn color="blue darken-1" text @click="save">
                Speichern
              </v-btn>

            </v-card-actions>

          </v-card-text>

        </v-card>

      </v-col>

    </v-row>

  </v-container>


</template>

<script>

import TimerApiService from "@/plugins/timer-api";
import HoldTypeSelector from "@/components/HoldTypeSelector.vue";

export default {

  components: {HoldTypeSelector},

  props: {
    selectedRound: {
      type: String
    }
  },

  data: () => ({

    competitorRounds: [],

    selectedCompetitorRoundIndex: null,
    selectedCompetitorRound: null,

    holdTypes: [],

    holdTypeSelected: null,

  }),

  created() {
    this.loadRounds();
    this.getHoldTypes();
  },

  watch: {
    selectedRound: function (newVal) {
      this.loadCompetitors(newVal);
    },

    selectedCompetitorRoundIndex: function (newVal) {
      this.selectedCompetitorRound = this.sortedCompetitorRounds[newVal]
    },

    holdTypeSelected(val) {
      this.selectedCompetitorRound.holdType = val;
    },

  },

  methods: {

    loadCompetitors(id) {

      TimerApiService.getCompetitorRounds(id)

          .then(response => {
            this.competitorRounds = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },

    save() {

      this.selectedCompetitorRound.competitorRoundStatus = 'COMPLETED';

      TimerApiService.updateCompetitorRound(this.selectedCompetitorRound)
          .then(response => {
            this.selectedCompetitorRound.score = response.data;
            this.selectedCompetitorRoundIndex += 1;
          })
    },

    getHoldTypes() {
      TimerApiService.getHoldTypes()
          .then(response => {
            this.holdTypes = response.data;
          });
    }

  },

  computed: {
    sortedCompetitorRounds() {
      return this.competitorRounds.slice().sort((a, b) => {
        return a.competitor.startNumber - b.competitor.startNumber;
      });
    }
  }

}

</script>