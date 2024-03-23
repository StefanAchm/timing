<template>
  <v-data-table :headers="headers" :items="rounds" sort-by="roundNumber" class="elevation-1">

    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Runden</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="1000px">

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

                  <v-col>
                    <v-text-field v-model="editedItem.roundNumber" label="Rundennummer"></v-text-field>
                  </v-col>
                  <v-col>
                    <v-text-field v-model="editedItem.maxHolds" label="Anzahl Griffe"></v-text-field>
                  </v-col>

                  <v-col>

                    <v-select
                        v-model="editedItem.gender"
                        :items="['HERREN', 'DAMEN']"
                        label="Geschlecht"/>

                  </v-col>

                </v-row>


                <v-row v-if="editedItem.score">

                  <v-col>

                    <HoldTypeSelector
                        :hold-type.sync="holdTypeSelected"
                        :selectDisabled="scoreRowDisabled()"
                    />

                  </v-col>

                  <v-col>

                    <v-text-field
                        :disabled="scoreRowDisabled()"
                        v-model="editedItem.score.holdNumber"
                        label="Griff Nummer"></v-text-field>

                  </v-col>

                  <v-col>

                    <v-text-field
                        :disabled="scoreRowDisabled()"
                        v-model="editedItem.score.tryNumber"
                        label="Versuch"></v-text-field>

                  </v-col>


                </v-row>

                <v-row v-if="editedItem">

                  <v-col cols="12" sm="6" md="4">
                    <span>Anzahl Teilnehmer: {{ maxNumberOfCompetitors }}</span>
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

        <DeleteDialog
            :dialog.sync="dialogDelete"
            @dialog-closed="initialize()"
            :itemprop="editedItem"
            :type="'selectedRoundId'"
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
import HoldTypeSelector from "@/components/HoldTypeSelector.vue";


export default {

  components: {HoldTypeSelector, DeleteDialog},

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
      score: {
        holdType: null,
        holdNumber: null,
        tryNumber: null
      }
    },
    defaultItem: {
      score: {}
    },
    menu: false,
    activePicker: null,

    holdTypeSelected: null,

    maxNumberOfCompetitors: 0

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

    holdTypeSelected(val) {
      this.editedItem.score.holdType = val;
    },

    editedItem() {

      axios.post(Properties.API_IP + '/round/preview', this.editedItem)
          .then(response => {
            this.maxNumberOfCompetitors = response.data.numberOfCompetitors;
          });


    }

  },

  created() {
    this.initialize();
  },

  mounted() {
    this.initialize();
  },

  methods: {

    scoreRowDisabled() {
      return this.editedItem.roundNumber <= 1 || !this.editedItem.roundNumber;
    },

    initialize() {
      axios
          .get(Properties.API_IP + '/round/getRounds')
          .then(response => {
            this.rounds = response.data;
          });

      this.editedItem = Object.assign({}, this.defaultItem)

    },

    editItem(item) {
      this.editedIndex = this.rounds.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },


    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
        this.initialize()
      })
    },

    deleteItem(item) {
      this.editedIndex = this.rounds.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },


    save() {

      if (this.editedIndex > -1) {

        axios.post(Properties.API_IP + '/round/update', this.editedItem).then(() => {this.close()});

      } else {

        axios.post(
            Properties.API_IP + '/round/create',
            this.editedItem,
            {params: {addCompetitors: true}}
        ).then(() => {this.close()});

      }

    },

  }


}
</script>