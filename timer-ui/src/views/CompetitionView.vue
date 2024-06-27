<template>

  <v-container>

    <RoundSelector
        :selectedRound.sync="selectedRound"
    />

    <CompetitorsList
        :selectedRound="selectedRound"
    />

    <CompetitorSimpleTable></CompetitorSimpleTable>

  </v-container>


</template>

<script>

import {defineComponent} from "vue";
import CompetitorsList from "@/components/competitor/CompetitorsList.vue";
import RoundSelector from "@/components/round/RoundSelector.vue";
import CompetitorSimpleTable from "@/components/competitor/CompetitorSimpleTable.vue";


export default defineComponent({

  components: {CompetitorSimpleTable, RoundSelector, CompetitorsList},

  data() {
    return {

      selectedRound: null,

    }
  },

})
</script>