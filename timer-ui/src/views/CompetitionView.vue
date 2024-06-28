<template>

  <v-container>

    <CompetitionHeader
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
import CompetitorRoundCard from "@/components/competitorRound/CompetitorRoundCard.vue";
import CompetitionHeader from "@/components/competition/CompetitionHeader.vue";


export default defineComponent({

  components: {CompetitorRoundCard, CompetitionHeader, CompetitorSimpleTable},

  data() {
    return {

      selectedRoundId: null,
      selectedCompetitorRound: null,

    }
  }

})
</script>