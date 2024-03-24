<template>


  <v-dialog v-model="dialogLocal" max-width="1000px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>
        <v-container>

          <v-row>

            <v-col>
              <v-text-field v-model="roundLocal.roundNumber" label="Rundennummer"></v-text-field>
            </v-col>
            <v-col>
              <v-text-field v-model="roundLocal.maxHolds" label="Anzahl Griffe"></v-text-field>
            </v-col>

            <v-col>

              <v-select
                  v-model="roundLocal.gender"
                  :items="['HERREN', 'DAMEN']"
                  label="Geschlecht"/>

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
                  :disabled="scoreRowDisabled()"
                  v-model="roundLocal.score.holdNumber"
                  label="Griff Nummer"></v-text-field>

            </v-col>

            <v-col>

              <v-text-field
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
import {Properties} from "@/config";
import axios from "axios";
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
      return this.editedIndex === -1 ? 'Ändern' : 'Neu'
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
      if (val && this.round && this.round.id) {
        this.roundLocal = Object.assign({}, this.round);
      }
    },

    holdTypeSelected(val) {
      this.roundLocal.score.holdType = val;
    },

    roundLocal() {


      axios.post(Properties.API_IP + '/round/preview', this.roundLocal)
          .then(response => {
            this.maxNumberOfCompetitors = response.data.numberOfCompetitors;
          }).catch(() => {});


    }

  },
  methods: {

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

      if (this.roundLocal.id) {

        axios.post(Properties.API_IP + '/round/update', this.roundLocal).then(() => {
          this.close()
        });

      } else {

        axios.post(
            Properties.API_IP + '/round/create',
            this.roundLocal,
            {params: {addCompetitors: true}}
        ).then(() => {
          this.close()
        });

      }

    },

  }


}
</script>