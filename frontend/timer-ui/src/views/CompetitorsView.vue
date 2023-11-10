<template>
  <v-data-table
      :headers="headers"
      :items="competitors"
      sort-by="calories"
      class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
          flat
      >
        <v-toolbar-title>Competitors</v-toolbar-title>
        <v-divider
            class="mx-4"
            inset
            vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
            v-model="dialog"
            max-width="500px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                dark
                class="mb-2"
                v-bind="attrs"
                v-on="on"
            >
              Add competitor
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.startNumber"
                        label="Start number"
                    ></v-text-field>
                  </v-col>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.firstName"
                        label="First Name"
                    ></v-text-field>
                  </v-col>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.lastName"
                        label="Last Name"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.city"
                        label="City"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.club"
                        label="Club"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-menu
                        ref="menu"
                        v-model="menu"
                        :close-on-content-click="false"
                        transition="scale-transition"
                        offset-y
                        min-width="auto"
                    >

                      <template v-slot:activator="{ on, attrs }">

                        <v-text-field
                            v-model="editedItem.dateOfBirth"
                            label="Date of Birth"
                            v-bind="attrs"
                            v-on="on"
                        ></v-text-field>

                      </template>

                      <v-date-picker
                          v-model="editedItem.dateOfBirth"
                          label="Date of Birth"
                          :active-picker.sync="activePicker"
                      ></v-date-picker>

                    </v-menu>

                  </v-col>

                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editedItem.gender"
                        label="Gender"
                    ></v-text-field>
                  </v-col>

                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="close"
              >
                Cancel
              </v-btn>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="save"
              >
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this item?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.actions`]="{ item }">
      <v-icon
          small
          class="mr-2"
          @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
      <v-icon
          small
          @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
    </template>
    <template v-slot:no-data>
      <v-btn
          color="primary"
          @click="initialize"
      >
        Reset
      </v-btn>
    </template>
  </v-data-table>
</template>

<script>
import {Properties} from "@/config";
import axios from "axios";

export default {

  data: () => ({
    dialog: false,
    dialogDelete: false,
    competitors: [],
    headers: [
      {text: 'Start Number', value: 'startNumber'},
      {text: 'First Name', value: 'firstName'},
      {text: 'Last Name', value: 'lastName'},
      {text: 'City', value: 'city'},
      {text: 'Club', value: 'club'},
      {text: 'Date of Birth', value: 'dateOfBirth'},
      {text: 'Gender', value: 'gender'},
      {text: 'Actions', value: 'actions', sortable: false}
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
    },
    menu: false,
    activePicker: null,
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
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
    },

    editItem(item) {
      this.editedIndex = this.competitors.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.competitors.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm() {

      axios
          .post(Properties.API_IP + '/competitor/delete', this.editedItem, {params: {soft: true}})
          .then(data => {
            console.log(data);
          })
          .catch(error => {
            console.log(error);
          }).finally(() => {
        this.dialogDelete = false
        this.close();
      });
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