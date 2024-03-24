<template>

  <v-snackbar
      v-model="showLocal"
      :color="color"
      :timeout="6000"
      :right="true"
      :bottom="true"
  >

    {{ message }}

    <v-btn color="white" text @click="close()">Close</v-btn>

  </v-snackbar>


</template>

<script>

export default {

  props: {
    show: Boolean,
    message: String,
    color: String,
  },

  data: () => ({

  }),

  computed: {
    showLocal: {
      get() {
        return this.show
      },
      set(value) {
        this.$emit('update:show', value)
      }
    },

  },

  methods: {
    close() {
      this.showLocal = false
    }
  }

};
</script>
