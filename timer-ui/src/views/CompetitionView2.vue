<template>

  <v-container>

    <RoundSelectorHeader
        :selectedRoundId.sync="selectedRoundId"
    />

    <v-row>

      <v-col>
        <CompetitorSimpleTable
            :roundId="selectedRoundId"
            :selectedCompetitorRound.sync="selectedCompetitorRound"
        />

      </v-col>

      <v-col>
        <CompetitorRound
            :competitor-round="selectedCompetitorRound"
        ></CompetitorRound>
      </v-col>

    </v-row>

  </v-container>


</template>

<script>

import {defineComponent} from "vue";
import CompetitorSimpleTable from "@/components/competitor/CompetitorSimpleTable.vue";
import RoundSelectorHeader from "@/components/round/RoundSelectorHeader.vue";
import CompetitorRound from "@/components/competitorRound/CompetitorRound.vue";


export default defineComponent({

  components: {CompetitorRound, RoundSelectorHeader, CompetitorSimpleTable},

  data() {
    return {

      selectedRoundId: null,
      selectedCompetitorRound: null,

    }
  }

})
</script>