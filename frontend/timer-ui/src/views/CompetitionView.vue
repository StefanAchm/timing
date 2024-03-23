<template>

  <v-container>

    <RoundSelector
        :selectedRound.sync="selectedRound"
    />

    <!-- TODO: set prop -->
    <CompetitorsList
        :selectedRound="selectedRound"
    />

    <CompetitorSimpleTable></CompetitorSimpleTable>

  </v-container>


</template>

<script>

import {defineComponent} from "vue";
import CompetitorsList from "@/components/CompetitorsList.vue";
import RoundSelector from "@/components/RoundSelector.vue";
import CompetitorSimpleTable from "@/components/CompetitorSimpleTable.vue";


export default defineComponent({

  components: {CompetitorSimpleTable, RoundSelector, CompetitorsList},

  data() {
    return {

      selectedRound: null,

    }
  },

})
</script>