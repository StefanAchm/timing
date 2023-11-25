<template>
  <v-data-table :headers="headers" :items="rounds" sort-by="roundNumber" class="elevation-1">

    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Runden</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Runde hinzufügen</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.roundNumber" label="Rundennummer"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.maxHolds" label="Anzahl Griffe"></v-text-field>
                  </v-col>

                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.gender" label="Gender"></v-text-field>
                  </v-col>

                  <v-col cols="12" sm="6" md="4">
                    <span>Anzahl Teilnehmer: {{ editedItem.competitors }}</span>
                    <!--                    <v-text-field :readonly="true" v-model="editedItem.competitors" label="Anzahl Teilnehmer"></v-text-field>-->
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


export default {

  data: () => ({
    dialog: false,
    dialogDelete: false,
    rounds: [],
    headers: [
      {text: 'Rundennummer', value: 'roundNumber'},
      {text: 'Typ', value: 'gender'},
      {text: 'Anzahl Griffe', value: 'maxHolds'},
      {text: 'Anzahl Teilnehmer', value: 'numberOfCompetitors'},
      {text: 'Aktionen', value: 'actions', sortable: false}
    ],
    editedIndex: -1,
    editedItem: {
      roundNumber: '',
      gender: '',
      maxHolds: ''
    },
    defaultItem: {
      name: '',
    },
    menu: false,
    activePicker: null,
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New' : 'Edit'
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
          .get(Properties.API_IP + '/round/getRounds')
          .then(response => {
            console.log(response.data);
            this.rounds = response.data;
          })
          .catch(error => {
            console.log(error);
          });
    },

    editItem(item) {
      this.editedIndex = this.rounds.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.rounds.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm() {

      axios
          .post(Properties.API_IP + '/round/delete', this.editedItem, {params: {soft: true}})
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

        axios
            .post(Properties.API_IP + '/round/update', this.editedItem)
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

        // New

        const createRoundRequest = {
          score: {
            holdType: null,
            holdNumber: "",
            tryNumber: "",
          },
          roundRequest: this.editedItem
        }

        axios
            .post(Properties.API_IP + '/round/create', createRoundRequest, {params: {addCompetitors: true}})
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