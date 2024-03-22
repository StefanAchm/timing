<template>

  <v-container>

    <RoundSelector
        :selectedRound.sync="selectedRound"
    />

    <!-- TODO: set prop -->
    <CompetitorsList
        :selectedRound="selectedRound"
    />

  </v-container>


</template>

<script>

import {defineComponent} from "vue";
import CompetitorsList from "@/components/CompetitorsList.vue";
import RoundSelector from "@/components/RoundSelector.vue";


export default defineComponent({

  components: {RoundSelector, CompetitorsList},

  data() {
    return {

      selectedRound: null,

    }
  },

})
</script>