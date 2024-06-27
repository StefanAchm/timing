<template>

  <v-data-table
      :headers="headers"
      sort-by="competitor.startNumber"
      item-key="competitor.startNumber"
      :items="competitorRounds"
  >


  </v-data-table>

</template>

<script>

export default {

  props: {
    competitorRounds: {
      type: Array,
      required: true
    }
  },

  data: () => ({

    headers: [
      {text: '#', value: 'competitor.startNumber', sortable: true},
      // {text: '+/-', value: 'competitorNumber', sortable: false},
      {text: 'Vorname', value: 'competitor.firstName', sortable: true},
      {text: 'Nachname', value: 'competitor.lastName', sortable: true},


      {text: 'Griffnummer', value: 'holdNumber', sortable: true},
      {text: 'Griffart', value: 'holdType', sortable: true},
      {text: 'Versuch', value: 'tryNumber', sortable: true},

      {text: 'Punkte', value: 'score', sortable: true},

      {text: '', value: 'action', sortable: false},
    ],

  }),

  created() {

  },

  methods: {

  }

}

</script>