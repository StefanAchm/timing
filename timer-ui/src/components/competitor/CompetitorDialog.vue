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

        <v-form
            autocomplete="off"
            v-model="isValid"
            ref="competitorDialogForm">

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

                <v-combobox
                    autocomplete="off"
                    v-model="selectedCompetitor"
                    :items="possibleCompetitors"
                    item-text="description"
                    item-value="description"
                    label="Vorname"
                    :rules="[v => !!v || 'Vorname darf nicht leer sein']"
                    required
                    @change="selectCompetitor"
                    clearable
                    @click:clear="init()"
                >
                  <template v-slot:item="data">
                    <v-card-text
                        :style="{
                          backgroundColor: data.item.gender === 'HERREN' ? 'var(--v-boys-base)' : (data.item.gender === 'DAMEN' ? 'var(--v-girls-base)' : 'transparent'),
                          display: 'block',
                          width: '100%',
                          padding: '10px'
                    }"
                    >
                      {{ data.item.description }}
                    </v-card-text>
                  </template>
                </v-combobox>

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

        <v-btn
            v-if="showDevButton && fullEdit"
            color="warning"
            @click="addRandom"
        >Zufällig
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn
            color="neutral"
            @click="close"
        >Schließen
        </v-btn>

        <v-btn
            color="primary"
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

    selectedCompetitor: null,
    possibleCompetitors: []

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

    showDevButton() {
      return window.env.VUE_APP_DEV_ENVIRONMENT
    }

  },

  watch: {
    dialog(val) {

      if (val) {

        if(this.competitor?.id) {

          this.competitorLocal = Object.assign({}, this.competitor);
          this.selectedCompetitor = this.competitorLocal.firstName;

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

      TimerApiService.getPossibleCompetitors()
          .then(response => {

            this.possibleCompetitors = response.data;

            // Sort by firstName
            this.possibleCompetitors.sort((a, b) => {
              if (a.firstName < b.firstName) return -1;
              if (a.firstName > b.firstName) return 1;
              return 0;
            });

            for (let i = 0; i < this.possibleCompetitors.length; i++){
              const element = this.possibleCompetitors[i];
              element.description = element.firstName + ' ' + element.lastName + ' (' + element.city + ') ' + (element.gender === 'HERREN' ? '♂️' : '♀️');
              element.selectId = i; 
            }

          })
          .catch(() => {});

    },

    selectCompetitor() {

      let startNumber = this.competitorLocal.startNumber;
      let paymentStatus = this.competitorLocal.paymentStatus;

      if(this.selectedCompetitor.selectId !== undefined) {
        this.competitorLocal = Object.assign({}, this.selectedCompetitor);
        this.selectedCompetitor = this.selectedCompetitor.firstName;
      } else {
        this.competitorLocal = {};
        this.competitorLocal.firstName = this.selectedCompetitor;
      }

      this.competitorLocal.startNumber = startNumber;
      this.competitorLocal.paymentStatus = paymentStatus;

    },

    addRandom() {

      TimerApiService
          .getStartNumber()
          .then(response => {

            let gender = Math.random() > 0.5 ? 'HERREN' : 'DAMEN';

            let firstName = gender === 'HERREN' ? 'Max' : 'Maria';

            let randomItem = {
              startNumber: response.data,
              firstName: firstName + Math.floor(Math.random() * 100),
              lastName: 'Mustermann' + Math.floor(Math.random() * 100),
              city: 'Musterstadt'  + Math.floor(Math.random() * 100),
              club: 'Musterclub' + Math.floor(Math.random() * 100),
              gender: gender,
              dateOfBirth: '1994-01-01',
            };


            TimerApiService
                .createCompetitor(randomItem)
                .then()
                .catch()
                .finally(() => {
                  this.$emit('dialog-closed', this.competitorLocal)
                  this.init()
                });

          })
          .catch(() => {});

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
      this.$refs.competitorDialogForm.reset(); // reset the form validation
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

            this.$refs.competitorDialogForm.reset(); // reset the form validation

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