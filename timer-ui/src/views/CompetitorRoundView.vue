<template>
  <div>
    <ApplicationHeader @update="refreshCompetitorRoundTable"/>
    <CompetitorRoundTable ref="competitorRoundTable"/>
  </div>
</template>

<script>
import CompetitorRoundTable from "@/components/competitorRound/CompetitorRoundTable.vue";
import ApplicationHeader from "@/components/ApplicationHeader.vue";

export default {

  components: {ApplicationHeader, CompetitorRoundTable},

  data: () => ({
    tableKey: 0
  }),

  created() {

  },

  methods: {
    refreshCompetitorRoundTable() {
      // Call a method on CompetitorRoundTable using $refs
      this.$refs.competitorRoundTable.init(); // assuming refreshData is a method in CompetitorRoundTable
    }
  }

}

</script>