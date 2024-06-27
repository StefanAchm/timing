<template>
  <v-data-table
      :headers="headers"
      :items="rounds"
      sort-by="roundNumber"
      class="elevation-1"
  >

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
      <v-icon small class="mr-2" @click="deleteItem(item)">mdi-delete</v-icon>
      <v-icon small class="mr-2" @click="goToCompetitorRounds(item)">mdi-open-in-new</v-icon>
    </template>

    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize">Reset</v-btn>
    </template>

  </v-data-table>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

import DeleteDialog from "@/components/DeleteDialog.vue";
import RoundDialog from "@/components/round/RoundDialog.vue";


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
      {text: 'Abgeschlossene Teilnehmer', value: 'completedCompetitors'},
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

    goToCompetitorRounds(item) {
      console.log(item)
      this.$router.push({name: 'round', params: {roundId: item.id}})
    },

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

      TimerApiService.getRounds()
          .then(response => {
            this.rounds = response.data;
          });

    },

  }

}
</script>