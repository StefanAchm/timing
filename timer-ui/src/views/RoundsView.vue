<template>
  <div>
    <ApplicationHeader/>
    <RoundTable/>
  </div>
</template>

<script>

import {defineComponent} from "vue";
import RoundTable from "@/components/round/RoundTable.vue";
import ApplicationHeader from "@/components/ApplicationHeader.vue";

export default defineComponent({
  components: {ApplicationHeader, RoundTable}
})
</script>