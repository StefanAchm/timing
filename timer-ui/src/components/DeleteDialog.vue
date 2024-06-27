<template>

  <v-dialog v-model="dialogVisible" max-width="500px">
    <v-card>
      <v-card-title class="text-h5">Wirklich Löschen?</v-card-title>

      <v-card-text v-if="warning">
        <p>{{ warning }}</p>

      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="close">Abbrechen</v-btn>
        <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>

</template>

<script>

import TimerApiService from "@/plugins/timer-api";

export default {

  props: {
    dialog: Boolean,
    itemprop: Object,
    type: String,
    warning: String
  },

  data() {
    return {}
  },

  methods: {

    close(cf) {
      this.dialogVisible = false
      this.$emit('dialog-closed', cf)
    },


    deleteItemConfirm() {

      TimerApiService.delete(this.type, this.editItem.id)
          .finally(() => {
            this.close(this.editItem)
          });

    },

  },

  computed: {
    dialogVisible: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

    editItem: {
      get() {
        return this.itemprop ? this.itemprop : new Object()
      },
      set(value) {
        this.$emit('update:itemProp', value)
      }
    }

  }

}

</script>