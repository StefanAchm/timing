<template>

  <v-data-table :headers="headers" :items="competitors" sort-by="startNumber" class="elevation-1">

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Teilnehmer</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">TeilnehmerInn hinzufügen</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>

                  <v-col>
                    <v-text-field
                        append-outer-icon="mdi-reload"
                        @click:append-outer="generateStartNumber()"
                        type="number"
                        v-model="editedItem.startNumber"
                        label="Startnummer">

                    </v-text-field>
                  </v-col>

                </v-row>
                <v-row>
                  <v-col>
                    <v-text-field v-model="editedItem.firstName" label="Vorname"></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col>
                    <v-text-field v-model="editedItem.lastName" label="Nachname"></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col>
                    <v-text-field v-model="editedItem.city" label="Stadt"></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col>
                    <v-text-field v-model="editedItem.club" label="Verein"></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col>

                    <v-dialog
                        ref="dialog"
                        v-model="modal"
                        :return-value.sync="editedItem.dateOfBirth"
                        persistent
                        width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                            v-model="editedItem.dateOfBirth"
                            label="Geburtsdatum"
                            append-outer-icon="mdi-calendar"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                          v-model="editedItem.dateOfBirth"
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
                            @click="$refs.dialog.save(editedItem.dateOfBirth)"
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
                        v-model="editedItem.gender"
                        :items="['Herren', 'Damen']"
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

        <DeleteDialog
            :dialog.sync="dialogDelete"
            @dialog-closed="initialize()"
            :itemprop="editedItem"
            :type="'competitor'"
        />


      </v-toolbar>
    </template>

    <template v-slot:[`item.actions`]="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
      <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
    </template>

    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize">Reset</v-btn>
    </template>

  </v-data-table>

</template>

<script>
import {Properties} from "@/config";
import axios from "axios";
import DeleteDialog from "@/components/DeleteDialog.vue";

export default {
  components: {DeleteDialog},

  data: () => ({
    startNumberCheckbox: false,
    dialogDelete: false,
    dialog: false,
    modal: false,

    competitors: [],
    headers: [
      {text: 'Startnummer', value: 'startNumber'},
      {text: 'Vorname', value: 'firstName'},
      {text: 'Nachname', value: 'lastName'},
      {text: 'Stadt', value: 'city'},
      {text: 'Verein', value: 'club'},
      {text: 'Geburtsdatum', value: 'dateOfBirth'},
      {text: 'Geschlect', value: 'gender'},
      {text: 'Aktionen', value: 'actions', sortable: false}
    ],
    editedIndex: -1,
    editedItem: {
      startNumber: '',
      firstName: '',
      lastName: '',
      city: '',
      club: '',
      dateOfBirth: '',
      gender: ''
    },
    defaultItem: {
      name: '',
      startNumber: '',
    },
    menu: false,
    activePicker: null,
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'Neu' : 'Ändern'
    },
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  created() {
    this.initialize();
  },

  mounted() {
    this.initialize();
  },

  methods: {

    initialize() {
      axios
          .get(Properties.API_IP + '/competitor/getCompetitors')
          .then(response => {
            console.log(response.data);
            this.competitors = response.data;
          })
          .catch(error => {
            console.log(error);
          });

      this.generateStartNumber()

    },

    generateStartNumber() {
      axios
          .get(Properties.API_IP + '/competitor/generateStartNumber')
          .then(response => {
            console.log(response.data);
            this.editedItem.startNumber = response.data;
          })
          .catch(error => {
            console.log(error);
          });
    },

    editItem(item) {
      this.editedIndex = this.competitors.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.generateStartNumber()
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.competitors.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
        this.initialize()
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
        this.initialize()
      })
    },

    save() {

      if (this.editedIndex > -1) {

        // Object.assign(this.competitors[this.editedIndex], this.editedItem)

        axios
            .post(Properties.API_IP + '/competitor/update', this.editedItem)
            .then(data => {
              console.log(data);
            })
            .catch(error => {
              console.log(error);
            }).finally(() => {
          this.close();
        });

        this.close()

      } else {

        // New competitor

        axios
            .post(Properties.API_IP + '/competitor/create', this.editedItem)
            .then(data => {
              console.log(data);
            })
            .catch(error => {
              console.log(error);
            }).finally(() => {
          this.close();
        });

      }


    },

  }


}
</script>