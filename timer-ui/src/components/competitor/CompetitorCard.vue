<template>

  <v-card>
    <v-card-title>
      <span class="text-h5">{{ formTitle }}</span>
    </v-card-title>

    <v-card-text>

      <v-form v-model="isValid">

        <v-container>

          <!--          <v-text-field-->
          <!--              :disabled="true"-->
          <!--              append-outer-icon="mdi-reload"-->
          <!--              @click:append-outer="generateStartNumber()"-->
          <!--              type="number"-->
          <!--              v-model="competitorLocal.startNumber"-->
          <!--              label="Startnummer"-->
          <!--              required-->
          <!--              :error-messages="startNumberErrors"-->
          <!--          >-->

          <!--          </v-text-field>-->

          <v-text-field
              v-model="competitorLocal.firstName"
              label="Vorname"
              :rules="[v => !!v || 'Vorname darf nicht leer sein']"
              required
          />


          <v-text-field
              v-model="competitorLocal.lastName"
              label="Nachname"
              :rules="[v => !!v || 'Nachname darf nicht leer sein']"
              required
          />

          <v-select
              v-model="competitorLocal.gender"
              :items="['HERREN', 'DAMEN']"
              label="Geschlecht"
              required
              :rules="[v => !!v || 'Geschlecht darf nicht leer sein']"
          />

          <BirthdayPickerMenu
              :date.sync="competitorLocal.dateOfBirth"
          ></BirthdayPickerMenu>


          <v-text-field
              v-model="competitorLocal.city"
              label="Stadt"
              required
          />


          <v-text-field
              v-model="competitorLocal.club"
              label="Verein"
              required
          />


        </v-container>

      </v-form>

    </v-card-text>

    <v-card-actions>

      <v-spacer></v-spacer>

      <v-btn
          color="primary"
          @click="save"
          :disabled="!isValid"
      >Speichern
      </v-btn>

    </v-card-actions>

  </v-card>

</template>

<script>
import BirthdayPickerMenu from "@/components/BirthdayPickerMenu.vue";
import TimerApiService from "@/plugins/timer-api";

export default {
  components: {BirthdayPickerMenu},

  props: {
    competitor: {
      type: Object,
    }
  },


  data: () => ({
    activePicker: 'YEAR',
    defaultCompetitor: {
      startNumber: '',
    },
    competitorLocal: {},
    startNumberErrors: [],
    isValid: false,

  }),

  computed: {

    formTitle() {
      return this.competitorLocal.id ? 'Ändern' : 'Neu'
    },

  },

  watch: {

    competitorLocal: {
      handler(val) {

        if (!val.startNumber) return;

        TimerApiService.isStartNumberValid(val)
            .then(response => {
              if (response.data && response.data === true) {
                this.startNumberErrors = [];
              } else {
                this.startNumberErrors = ['Startnummer ' + val.startNumber + ' bereits vergeben'];
              }
            })
            .catch(() => {
            });


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
          .catch(() => {
          });
    },

    close() {
      this.competitorLocal = this.defaultCompetitor;
    },

    save() {

      TimerApiService.updateOrCreateCompetitor(this.competitorLocal)
          .then(() => {

            let message =
                this.competitorLocal.firstName + ' ' + this.competitorLocal.lastName
                + ' mit Startnummer ' + this.competitorLocal.startNumber
                + ' gespeichert';

            this.$root.snackbar.showSuccess({message: message});

            this.init()

          })
          .catch(() => {
          });

    }

  },

}

</script>