<template>

  <v-dialog v-model="dialogVisible" max-width="500px">
    <v-card>
      <v-card-title class="text-h5">Wirklich Löschen?</v-card-title>
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

import axios from "axios";
import {Properties} from "@/config";

export default {

  props: {
    dialog: Boolean,
    itemprop: Object,
    type: String
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

      axios
          .post(Properties.API_IP + '/' + this.type + '/delete', this.editItem.id)
          .then()
          .catch()
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