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

import TimerApiService from "@/plugins/timer-api";

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
    this.loadHoldTypes();
  },

  watch: {},

  methods: {

    loadHoldTypes() {
      TimerApiService.getHoldTypes()
          .then(response => {
            
            // set the german values for all
            for(let holdtype of response.data) {
              let holdTypeItem = {
                id: holdtype,
                name: this.getName(holdtype),
              };
              this.holdTypes.push(holdTypeItem);
            }

            console.log(this.holdTypes);

          })
          .catch(() => {});
    },

    getName(holdtype) {
      if(holdtype === 'TOUCHED') {
        return "Berührt";
      } else if(holdtype === 'SLIPPED') {
        return "Abgerutscht"
      } else if(holdtype === 'HELD') {
        return "Gehalten"
      } else if(holdtype === 'FOLLOWED') {
        return "Gefolgt"
      } else if(holdtype === 'MOVED_ON') {
        return "Weitergezogen"
      }
    },

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