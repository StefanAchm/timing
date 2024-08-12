<template>


  <v-dialog
      v-model="dialogLocal"
      max-width="1000px"
      @click:outside="close"
  >

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>
        <v-container>

          <v-row>

            <v-col>

              <v-select
                  :disabled="!!round.id"
                  v-model="roundLocal.gender"
                  :items="['HERREN', 'DAMEN']"
                  label="Geschlecht"
                  @change="updateRoundNumber"
              />

            </v-col>

            <v-col>
              <v-text-field
                  type="number"
                  v-model="roundLocal.roundNumber"
                  label="Rundennummer"
                  :disabled="!!round.id"
              ></v-text-field>
            </v-col>
            <v-col>
              <v-text-field
                  type="number"
                  v-model="roundLocal.maxHolds"
                  label="Anzahl Griffe"></v-text-field>
            </v-col>



          </v-row>


          <v-row v-if="roundLocal.score">

            <v-col>

              <HoldTypeSelector
                  :hold-type.sync="holdTypeSelected"
                  :selectDisabled="scoreRowDisabled()"
              />

            </v-col>

            <v-col>

              <v-text-field
                  type="number"
                  :disabled="scoreRowDisabled()"
                  v-model="roundLocal.score.holdNumber"
                  label="Griff Nummer"></v-text-field>

            </v-col>

            <v-col>

              <v-text-field
                  type="number"
                  :disabled="scoreRowDisabled()"
                  v-model="roundLocal.score.tryNumber"
                  label="Versuch"></v-text-field>

            </v-col>


          </v-row>

          <v-row v-if="roundLocal">

            <v-col cols="12" sm="6" md="4">
              <span>Anzahl Teilnehmer: {{ maxNumberOfCompetitors }}</span>
            </v-col>

          </v-row>

        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="close">
          Cancel
        </v-btn>
        <v-btn color="blue darken-1" text @click="save">
          Save
        </v-btn>
      </v-card-actions>

    </v-card>

  </v-dialog>


</template>

<script>

import TimerApiService from "@/plugins/timer-api";

import HoldTypeSelector from "@/components/HoldTypeSelector.vue";


export default {

  components: {HoldTypeSelector},


  props: {
    dialog: {
      type: Boolean,
      default: false
    },
    round: {
      type: Object,
    },
    rounds: {
      type: Array,
    }
  },

  data: () => ({
    modal: false,
    roundLocal: {
      score: {
        holdType: null,
        holdNumber: null,
        tryNumber: null
      }
    },
    maxNumberOfCompetitors: 0,
    holdTypeSelected: null
  }),

  computed: {

    formTitle() {
      return this.round.id ? 'Ändern' : 'Neu'
    },

    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

  },

  watch: {

    dialog(val) {
      if (val) {
        this.roundLocal = Object.assign({}, this.round);
      }
    },

    holdTypeSelected(val) {
      this.roundLocal.score.holdType = val;
    },

    // watch deep, use:

    roundLocal: {
      handler(newVal) {

        if(newVal.score === null && newVal.roundNumber > 1) {
          return;
        }

        TimerApiService.previewRound(newVal)
            .then(response => {
              this.maxNumberOfCompetitors = response.data?.numberOfCompetitors;
            })
            .catch(() => {});
      },
      deep: true
    }

  },

  methods: {

    updateRoundNumber() {

      // If the round number is already set, do nothing, otherwise set the next round number

      let highestRoundNumber = this.rounds
          .filter(round => round.gender === this.roundLocal.gender)
          .map(round => round.roundNumber)
          .reduce((a, b) => Math.max(a, b), 0);

      this.roundLocal.roundNumber = highestRoundNumber + 1;

    },

    scoreRowDisabled() {
      return this.roundLocal.roundNumber <= 1 || !this.roundLocal.roundNumber;
    },

    close(cf) {
      this.dialogLocal = false
      this.roundLocal = {
        score: {
          holdType: null,
          holdNumber: null,
          tryNumber: null
        }
      };
      this.$emit('dialog-closed', cf)
    },

    save() {

      TimerApiService.updateOrCreateRound(this.roundLocal)
          .then(response => {
            this.roundLocal = response.data;
            this.$root.snackbar.showSuccess({message: 'Gespeichert'})
            this.close()
          })
          .catch(() => {});
    }

  }

}
</script>