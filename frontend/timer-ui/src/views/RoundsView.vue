<template>
  <v-data-table :headers="headers" :items="rounds" sort-by="roundNumber" class="elevation-1">

    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Runden</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>

        <v-spacer></v-spacer>

        <v-btn
            color="primary"
            dark
            class="mb-2"
            @click="addDialog = true"
        >Runde hinzufügen</v-btn>

        <RoundDialog
            :dialog.sync="addDialog"
            :round="{}"
            @dialog-closed="initialize()"
        />

        <DeleteDialog
            :dialog.sync="dialogDelete"
            @dialog-closed="initialize()"
            :itemprop="editedItem"
            :type="'round'"
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

import timerService from "@/plugins/timerService";

import DeleteDialog from "@/components/DeleteDialog.vue";
import RoundDialog from "@/components/RoundDialog.vue";


export default {

  components: {RoundDialog, DeleteDialog},

  data: () => ({
    dialog: false,
    dialogDelete: false,
    addDialog: false,

    rounds: [],
    headers: [
      {text: 'Rundennummer', value: 'roundNumber'},
      {text: 'Typ', value: 'gender'},
      {text: 'Anzahl Griffe', value: 'maxHolds'},
      {text: 'Anzahl Teilnehmer', value: 'numberOfCompetitors'},
      {text: 'Aktionen', value: 'actions', sortable: false}
    ],

    editedItem: {
      score: {
        holdType: null,
        holdNumber: null,
        tryNumber: null
      }
    },

  }),

  computed: {},

  watch: {},

  created() {
    this.initialize();
  },

  mounted() {
    this.initialize();
  },

  methods: {

    editItem(item) {
      this.editedIndex = this.competitors.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogVisible = true
    },

    deleteItem(item) {
      this.editedIndex = this.rounds.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    initialize() {

      timerService.getRounds()
          .then(response => {
            this.rounds = response.data;
          });

    },

  }

}
</script>