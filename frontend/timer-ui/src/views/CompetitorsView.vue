<template>

  <v-container>

    <v-alert
        prominent
        type="warning"
        v-if="competitors.filter(competitor => competitor.nrOfRounds === 0).length > 0"
    >
      <v-row align="center">
        <v-col class="grow">
          Einige Teilnehmer ({{ competitors.filter(competitor => competitor.nrOfRounds === 0).length }}) haben noch
          keine Runden zugeordnet.
        </v-col>
        <v-col class="shrink">
          <v-btn @click="autoAdd()">Automatisch zuordnen</v-btn>
        </v-col>
      </v-row>
    </v-alert>

    <v-data-table
        :headers="headers"
        :items="competitors"
        sort-by="startNumber"
        item-key="startNumber"
        class="elevation-1">

      <template v-slot:top>

        <v-toolbar flat>

          <v-toolbar-title>Teilnehmer</v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>

          <v-spacer></v-spacer>

          <v-btn color="primary" dark class="mb-2" @click="addRandom()">Add Random</v-btn>

          <v-spacer></v-spacer>

          <v-btn
              color="primary"
              dark
              class="mb-2"
              @click="dialogVisible = true"
          >TeilnehmerInn hinzufügen
          </v-btn>

          <CompetitorDialog
              :competitor.sync="editedItem"
              :dialog.sync="dialogVisible"
              @dialog-closed="initialize()"
              :full-edit="true"/>

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

  </v-container>

</template>

<script>
import DeleteDialog from "@/components/DeleteDialog.vue";
import CompetitorDialog from "@/components/CompetitorDialog.vue";

import timerService from "@/plugins/timerService";

export default {
  components: {CompetitorDialog, DeleteDialog},

  data: () => ({

    dialogDelete: false,
    dialogVisible: false,

    competitors: [],
    headers: [
      {text: 'Startnummer', value: 'startNumber'},
      {text: 'Vorname', value: 'firstName'},
      {text: 'Nachname', value: 'lastName'},
      {text: 'Stadt', value: 'city'},
      {text: 'Verein', value: 'club'},
      {text: 'Geburtsdatum', value: 'dateOfBirth'},
      {text: 'Geschlect', value: 'gender'},
      {text: 'Runden', value: 'nrOfRounds'},
      {text: 'Aktionen', value: 'actions', sortable: false}
    ],
    editedItem: {},
  }),

  computed: {},

  watch: {},

  created() {
    this.initialize();
  },

  methods: {

    initialize() {

      this.editedItem = {};

      timerService
          .getCompetitors()
          .then(response => {
            this.competitors = response.data;

            for (const element of this.competitors) {
              element.nrOfRounds = element.competitorRounds.length;
            }

          });

    },

    addRandom() {

      timerService
          .getStartNumber()
          .then(response => {


            let randomItem = {
              startNumber: response.data,
              firstName: 'Max' + Math.floor(Math.random() * 100),
              lastName: 'Mustermann',
              city: 'Musterstadt',
              club: 'Musterclub',
              gender: Math.random() > 0.5 ? 'HERREN' : 'DAMEN',
              dateOfBirth: '1994-02-17'
            };


            timerService
                .createCompetitor(randomItem)
                .then()
                .catch()
                .finally(() => {
                  this.initialize();
                });

          });

    },


    editItem(item) {
      this.editedItem = Object.assign({}, item)
      this.dialogVisible = true
    },

    deleteItem(item) {
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    autoAdd() {

      for (const competitor of this.competitors.filter(competitor => competitor.nrOfRounds === 0)) {

        timerService.addCompetitorRound(competitor.id, 1)
            .then(() => {
              this.initialize();
            });

      }


    }

  }

}
</script>