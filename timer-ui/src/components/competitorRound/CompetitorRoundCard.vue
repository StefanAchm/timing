<template>

  <v-container>

    <v-card v-if="competitorRoundLocal">

      <v-card-title>

        {{ competitorRoundLocal.competitor.firstName + ' ' + competitorRoundLocal.competitor.lastName }}

        <v-spacer></v-spacer>

        <v-icon @click="edit">mdi-pencil</v-icon>

        <CompetitorDialog
            :dialog.sync="dialogVisible"
            :competitor.sync="competitorRoundLocal.competitor"
            :full-edit="false"
        />


      </v-card-title>

      <v-card-subtitle>
        <strong> Startnummer: </strong> {{ competitorRoundLocal.competitor.startNumber }} <br>
        <strong> Geschlecht: </strong> {{ competitorRoundLocal.competitor.gender }} <br>
        <strong> Stadt: </strong> {{ competitorRoundLocal.competitor.city }} <br>
        <strong> Verein: </strong> {{ competitorRoundLocal.competitor.club }} <br>
        <strong> Geburtsdatum: </strong> {{ competitorRoundLocal.competitor.dateOfBirth }} <br>
      </v-card-subtitle>

      <v-divider class="mx-4"></v-divider>


      <v-card-text>
        <v-container>

          <v-row>

            <v-col>

              Runde {{ competitorRoundLocal.roundNumber }}

            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <HoldTypeSelector
                  :hold-type.sync="competitorRoundLocal.holdType"
                  :selectDisabled="false"
              />

            </v-col>

          </v-row>
          <v-row>
            <v-col>
              <v-text-field
                  type="number"
                  v-model="competitorRoundLocal.holdNumber"
                  :label="'Griffnummer (max:' + maxNumberOfHolds + ')'"
                  ></v-text-field>

            </v-col>
          </v-row>
          <v-row>
            <v-col>

              <v-text-field
                  type="number"
                  v-model="competitorRoundLocal.tryNumber"
                  label="Versuch"></v-text-field>

            </v-col>
          </v-row>
        </v-container>

        <v-card-actions>

          <v-spacer></v-spacer>

          <v-btn
              color="blue darken-1"
              text
              @click="saveTop">

            TOP

          </v-btn>

          <v-btn
              :disabled="saveDisabled"
              v-if="competitorRoundLocal.competitorRoundStatus !== 'COMPLETED'"
              color="blue darken-1"
              text
              @click="save">

            Speichern

          </v-btn>

          <v-btn
              v-else
              color="red darken-1" text @click="save">
            Überschreiben
          </v-btn>

        </v-card-actions>

      </v-card-text>

    </v-card>

    <v-card v-else>

      <v-card-title>
        Bitte einen Teilnehmer auswählen
      </v-card-title>

    </v-card>

  </v-container>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

import HoldTypeSelector from "@/components/HoldTypeSelector.vue";
import CompetitorDialog from "@/components/competitor/CompetitorDialog.vue";

export default {

  components: {CompetitorDialog, HoldTypeSelector},

  props: {
    competitorRound: {
      type: Object
    },
    maxNumberOfHolds: {
      type: Number
    }
  },

  data: () => ({

    holdTypes: [],
    dialogVisible: false,

  }),

  created() {

    TimerApiService.getHoldTypes()
        .then(response => {
          this.holdTypes = response.data;
        })
        .catch(() => {});

  },

  watch: {},

  methods: {

    save() {

      this.competitorRoundLocal.competitorRoundStatus = 'COMPLETED';

      TimerApiService.updateCompetitorRound(this.competitorRoundLocal)
          .then(response => {
            this.competitorRoundLocal.score = response.data;
            this.$root.snackbar.showSuccess({message: 'Gespeichert'})
            this.$emit('updated', this.competitorRoundLocal)
          })
          .catch(() => {});
    },

    saveTop() {

      this.competitorRoundLocal.competitorRoundStatus = 'COMPLETED';

      this.competitorRoundLocal.holdType = 'HELD';
      this.competitorRoundLocal.holdNumber = this.maxNumberOfHolds;
      this.competitorRoundLocal.tryNumber = 1;


      TimerApiService.updateCompetitorRound(this.competitorRoundLocal)
          .then(response => {
            this.competitorRoundLocal.score = response.data;
            this.$root.snackbar.showSuccess({message: 'Gespeichert'})
            this.$emit('updated', this.competitorRoundLocal)
          })
          .catch(() => {});
    },

    edit() {

      this.dialogVisible = true;

    },

    setRandomScore() {

      this.competitorRoundLocal.holdType = this.holdTypes[Math.floor(Math.random() * this.holdTypes.length)];
      this.competitorRoundLocal.holdNumber = Math.floor(Math.random() * 10) + 1;
      this.competitorRoundLocal.tryNumber = Math.floor(Math.random() * 3) + 1;

      this.save()

    }

  },

  computed: {

    saveDisabled() {
      return this.compcompetitorRoundLocal === null ||
          this.competitorRoundLocal.holdType === null
          || this.competitorRoundLocal.holdNumber === null
          || this.competitorRoundLocal.holdNumber < 1
          || this.competitorRoundLocal.tryNumber === null
          || this.competitorRoundLocal.tryNumber < 1
    },

    competitorRoundLocal: {
      get() {
        return this.competitorRound
      },
      set(value) {
        console.log(value)
        this.$emit('update:competitorRound', value)
      }
    },

  }

}

</script>