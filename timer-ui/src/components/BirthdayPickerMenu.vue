<template>


  <v-menu
      ref="menu"
      v-model="menu"
      :close-on-content-click="false"
      :return-value.sync="computedDate"
      transition="scale-transition"
      offset-y
      min-width="auto"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
          v-model="computedDate"
          label="Geburtsdatum"
          append-outer-icon="mdi-calendar"
          hint="YYYY-MM-DD"
          persistent-hint
          v-bind="attrs"
          v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker
        v-model="computedDate"
        no-title
        scrollable
        :active-picker.sync="activePicker"
        @change="save"
        :max="(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substring(0, 10)"
        min="1950-01-01"
    >

    </v-date-picker>
  </v-menu>

</template>

<script>
export default {

  props: {
    date: String,
  },

  data: () => ({
    // date: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    menu: false,
    activePicker: null,
  }),

  watch: {
    menu (val) {
      val && setTimeout(() => (this.activePicker = 'YEAR'))
    },
  },
  methods: {
    save (date) {
      this.$refs.menu.save(date)
    },
  },

  computed: {
    computedDate: {
      get() {
        return this.date
      },
      set(val) {
        this.$emit('update:date', val)
      },
    },
  },


}
</script>