<template>

  <v-select
      v-model="holdTypeLocal"
      :items="holdTypes"
      :disabled="selectDisabled"
      item-text="name"
      item-value="id"
      label="Griffart"

  ></v-select>

</template>

<script>

import timerService from "@/plugins/timerService";

export default {

  props: {
    holdType: {
      type: String
    },
    selectDisabled: {
      type: Boolean,
      default: true
    }
  },

  data: () => ({

    holdTypes: []

  }),



  created() {
    this.getHoldTypes();
  },

  watch: {},

  methods: {

    getHoldTypes() {
      timerService.getHoldTypes().then(response => {
            this.holdTypes = response.data;
          });
    }

  },

  computed: {
    holdTypeLocal: {
      get() {
        return this.holdType
      },
      set(value) {
        this.$emit('update:holdType', value)
      }
    },

  }


}

</script>