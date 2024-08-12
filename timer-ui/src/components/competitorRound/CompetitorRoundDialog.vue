<template>

  <v-dialog
      v-if="competitorRoundLocal"
      v-model="dialogLocal"
      max-width="500px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>

        <v-form>

          <v-container>

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
                    label="Griffnummer"></v-text-field>

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

        </v-form>

      </v-card-text>

      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn
            color="neutral"
            @click="close"
        >Abbrechen
        </v-btn>

        <v-btn
            color="primary"
            @click="save"
        >Speichern</v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

import HoldTypeSelector from "@/components/HoldTypeSelector.vue";
import TimerApi from "@/plugins/timer-api";

export default {
  components: {HoldTypeSelector},

  props: {
    dialog: {
      type: Boolean,
      default: false
    },
    competitorRound: {
      type: Object,
    }
  },

  data: () => ({

  }),

  methods: {

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },

    save() {

      this.competitorRoundLocal.competitorRoundStatus = 'COMPLETED';

      TimerApi.updateCompetitorRound(this.competitorRoundLocal)
          .then(response => {
            this.competitorRoundLocal.score = response.data;
            this.dialogLocal = false
            this.$emit('dialog-closed')
          })
          .catch(() => {});


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

    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

    formTitle() {
      return 'Teilnehmerrunde bearbeiten'
    },

  },


}


</script>