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
        <CompetitorRoundCard
            :competitor-round="selectedCompetitorRound"
        ></CompetitorRoundCard>
      </v-col>

    </v-row>

  </v-container>


</template>

<script>

import {defineComponent} from "vue";
import CompetitorSimpleTable from "@/components/competitor/CompetitorSimpleTable.vue";
import RoundSelectorHeader from "@/components/round/RoundSelectorHeader.vue";
import CompetitorRoundCard from "@/components/competitorRound/CompetitorRoundCard.vue";


export default defineComponent({

  components: {CompetitorRoundCard, RoundSelectorHeader, CompetitorSimpleTable},

  data() {
    return {

      selectedRoundId: null,
      selectedCompetitorRound: null,

    }
  }

})
</script>