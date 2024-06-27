<template>
  <CompetitorRoundTable
      :competitor-rounds="selectedCompetitorRounds"
  />
</template>

<script>
import CompetitorRoundTable from "@/components/competitorRound/CompetitorRoundTable.vue";
import TimerApi from "@/plugins/timer-api";

export default {

  components: {CompetitorRoundTable},

  data: () => ({

    selectedCompetitorRounds: [],

  }),

  created() {

    TimerApi
        .getCompetitorRounds(this.$route.params.roundId)
        .then(response => {
          this.selectedCompetitorRounds = response.data;
        })

  }

}

</script>