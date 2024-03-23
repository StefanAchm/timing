<template>

  <v-dialog v-model="dialogLocal" max-width="500px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-row>

            <v-col>
              <v-text-field
                  :disabled="!fullEdit"
                  append-outer-icon="mdi-reload"
                  @click:append-outer="generateStartNumber()"
                  type="number"
                  v-model="startNumber"
                  label="Startnummer">

              </v-text-field>
            </v-col>

          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorLocal.firstName" label="Vorname"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorLocal.lastName" label="Nachname"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorLocal.city" label="Stadt"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field v-model="competitorLocal.club" label="Verein"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>

              <v-dialog
                  ref="dialog"
                  v-model="modal"
                  :return-value.sync="competitorLocal.dateOfBirth"
                  persistent
                  width="290px"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="competitorLocal.dateOfBirth"
                      label="Geburtsdatum"
                      append-outer-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                    v-model="competitorLocal.dateOfBirth"
                    scrollable
                >
                  <v-spacer></v-spacer>
                  <v-btn
                      text
                      color="primary"
                      @click="modal = false"
                  >
                    Abbrechen
                  </v-btn>
                  <v-btn
                      text
                      color="primary"
                      @click="$refs.dialog.save(competitorLocal.dateOfBirth)"
                  >
                    OK
                  </v-btn>
                </v-date-picker>
              </v-dialog>

            </v-col>
          </v-row>

          <v-row>
            <v-col>

              <v-select
                  :disabled="!fullEdit"
                  v-model="competitorLocal.gender"
                  :items="['HERREN', 'DAMEN']"
                  label="Geschlecht"/>

            </v-col>

          </v-row>

        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="close">Abbrechen</v-btn>
        <v-btn color="blue darken-1" text @click="save">Speichern</v-btn>
      </v-card-actions>

    </v-card>
  </v-dialog>

</template>

<script>
import {Properties} from "@/config";
import axios from "axios";

export default {

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
    modal:false,
    startNumber: null,
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
    
    competitorLocal: {
      get() {
        console.log('get competitor', this.competitor)
        return this.competitor
      },
      set(value) {
        console.log('set competitor', value)
        this.$emit('update:competitor', value)
      }
    },

  },

  watch: {

    

  },

  created() {

    this.generateStartNumber()

  },

  methods: {

    generateStartNumber() {
      axios
          .get(Properties.API_IP + '/competitor/generateStartNumber')
          .then(response => {
            this.startNumber = response.data;
          });
    },

    close() {
      this.dialogLocal = false;
      this.competitorLocal = {};
    },

    save() {

      if (!this.competitorLocal.id) {

        axios.post(Properties.API_IP + '/competitor/update', this.competitorLocal);
        this.close()

      } else {

        axios.post(Properties.API_IP + '/competitor/create', this.competitorLocal)
        this.close();

      }

    },


  },

}
</script>