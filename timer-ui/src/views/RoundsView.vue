<template>
  <div>
    <ApplicationHeader @update="refreshRoundTable"/>
    <RoundTable ref="roundTable"/>
  </div>
</template>

<script>

import RoundTable from "@/components/round/RoundTable.vue";
import ApplicationHeader from "@/components/ApplicationHeader.vue";

export default {

  components: {ApplicationHeader, RoundTable},

  data: () => ({

  }),

  methods: {
    refreshRoundTable() {
      // Call a method on RoundTable using $refs
      this.$refs.roundTable.init(); // assuming refreshData is a method in RoundTable
    }
  }

}
</script>