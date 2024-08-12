<template>
  <div>
    <ApplicationHeader/>
    <CompetitorRoundTable/>
  </div>
</template>

<script>
import CompetitorRoundTable from "@/components/competitorRound/CompetitorRoundTable.vue";
import ApplicationHeader from "@/components/ApplicationHeader.vue";

export default {

  components: {ApplicationHeader, CompetitorRoundTable},

  data: () => ({

  }),

  created() {

  }

}

</script>