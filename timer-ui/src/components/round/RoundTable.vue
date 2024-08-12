<template>

  <div>

    <DeleteDialog
        :dialog.sync="dialogDelete"
        @dialog-closed="init()"
        :itemprop="editedItem"
        :type="'round'"
    />

    <v-data-table
        :headers="headers"
        :items="rounds"
        sort-by="roundNumber"
        class="elevation-1"
    >

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
        <v-icon small class="mr-2" @click="deleteItem(item)">mdi-delete</v-icon>
      </template>

    </v-data-table>

  </div>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

import DeleteDialog from "@/components/DeleteDialog.vue";


export default {

  components: {DeleteDialog},

  data: () => ({
    dialog: false,
    dialogDelete: false,
    roundDialog: false,

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
    this.init();
  },

  mounted() {
    this.init();
  },

  methods: {

    goToCompetitorRounds(item) {
      this.$router.push({name: 'round', params: {roundId: item.id}})
    },

    editItem(item) {
      this.editedItem = Object.assign({}, item)
      this.roundDialog = true
    },

    deleteItem(item) {
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    addItem() {
      this.editedItem = {
        score: {
          holdType: null,
          holdNumber: null,
          tryNumber: null
        }
      };
      this.roundDialog = true
    },

    init() {

      this.editedItem = {
        score: {
          holdType: null,
          holdNumber: null,
          tryNumber: null
        }
      };

      TimerApiService.getRounds()
          .then(response => {
            this.rounds = response.data;
          })
          .catch(() => {
          });

    },

  }

}
</script>