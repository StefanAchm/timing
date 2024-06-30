<template>

  <v-dialog
      v-if="competitorLocal"
      v-model="dialogLocal"
      max-width="500px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>

        <v-form v-model="isValid">

          <v-container>

            <v-row>

              <v-col>
                <v-text-field
                    :disabled="!fullEdit"
                    append-outer-icon="mdi-reload"
                    @click:append-outer="generateStartNumber()"
                    type="number"
                    v-model="competitorLocal.startNumber"
                    label="Startnummer"
                    required
                    :error-messages="startNumberErrors"
                >

                </v-text-field>
              </v-col>

              <v-col>

                <v-checkbox
                    v-model="competitorLocal.paymentStatus"
                    :false-value="'NOT_PAID'"
                    :true-value="'PAID'"
                    label="Bezahlt"
                ></v-checkbox>

              </v-col>

            </v-row>

            <v-row>

              <v-col>

                <v-text-field
                    v-model="competitorLocal.firstName"
                    label="Vorname"
                    :rules="[v => !!v || 'Vorname darf nicht leer sein']"
                    required
                />

              </v-col>

              <v-col>

                <v-text-field
                    v-model="competitorLocal.lastName"
                    label="Nachname"
                    :rules="[v => !!v || 'Nachname darf nicht leer sein']"
                    required
                />

              </v-col>

            </v-row>

            <v-row>

              <v-col>

                <v-select
                    :disabled="!fullEdit"
                    v-model="competitorLocal.gender"
                    :items="['HERREN', 'DAMEN']"
                    label="Geschlecht"
                    required
                    :rules="[v => !!v || 'Geschlecht darf nicht leer sein']"
                />

              </v-col>

              <v-col>

                <BirthdayPickerMenu
                    :date.sync="competitorLocal.dateOfBirth"
                ></BirthdayPickerMenu>

              </v-col>

            </v-row>

            <v-row>

              <v-col>

                <v-text-field
                    v-model="competitorLocal.city"
                    label="Stadt"
                    required
                />

              </v-col>

              <v-col>

                <v-text-field
                    v-model="competitorLocal.club"
                    label="Verein"
                    required
                />

              </v-col>

            </v-row>

          </v-container>

        </v-form>

      </v-card-text>

      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn
            color="blue darken-1"
            text
            @click="close"
        >Abbrechen
        </v-btn>

        <v-btn
            color="blue darken-1"
            text
            @click="save"
            :disabled="!isValid"
        >Speichern</v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

import BirthdayPickerMenu from "@/components/BirthdayPickerMenu.vue";

export default {
  components: {BirthdayPickerMenu},

  props: {
    dialog: {
      type: Boolean,
      default: false
    },
    competitor: {
      type: Object,
    },
    fullEdit: {
      type: Boolean,
      default: false
    }
  },


  data: () => ({
    activePicker: 'YEAR',
    defaultCompetitor: {
      startNumber: '',
      paymentStatus: 'PAID'
    },
    competitorLocal: {},
    startNumberErrors: [],
    isValid: false,
    closeDialogAfterSave: false,

  }),

  computed: {
    formTitle() {
      return this.competitorLocal.id ? 'Ändern' : 'Neu'
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

        if(this.competitor?.id) {

          this.competitorLocal = Object.assign({}, this.competitor);

        } else {

            this.init()

        }

      }

    },

    competitorLocal: {
      handler(val) {

        if(!val.startNumber) return;

        TimerApiService.isStartNumberValid(val)
            .then(response => {
              if (response.data && response.data === true) {
                this.startNumberErrors = [];
              } else {
                this.startNumberErrors = ['Startnummer ' + val.startNumber + ' bereits vergeben'];
              }
            })
            .catch(() => {});


      },
      deep: true
    }

  },

  created() {

    this.init()

  },

  methods: {

    init() {
      this.competitorLocal = Object.assign({}, this.defaultCompetitor);
      this.generateStartNumber()
    },

    generateStartNumber() {

      TimerApiService.getStartNumber()
          .then(response => {
            this.competitorLocal.startNumber = response.data;
          })
          .catch(() => {});
    },

    close(cf) {
      this.dialogLocal = false;
      this.competitorLocal = this.defaultCompetitor;
      this.$emit('dialog-closed', cf)
    },

    save() {

      TimerApiService.updateOrCreateCompetitor(this.competitorLocal)
          .then(() => {

              let message =
                  this.competitorLocal.firstName + ' ' + this.competitorLocal.lastName
                  + ' mit Startnummer ' + this.competitorLocal.startNumber
                  + ' gespeichert';

            this.$root.snackbar.showSuccess({message: message});

            if (this.competitorLocal.id || this.closeDialogAfterSave) {
              this.close()
            } else {
              this.$emit('dialog-closed', this.competitorLocal)
              this.init()
            }

          })
          .catch(() => {});

    }

  },

}
</script>