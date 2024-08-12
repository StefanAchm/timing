<template>


  <v-footer
      color="neutral"
  >


    <v-select
        v-model="selectedRoundId"
        :items="rounds"
        item-text="roundName"
        item-value="id"
    ></v-select>

    <RoundDialog
        :dialog.sync="addDialog"
        :round="editRound"
        @dialog-closed="initialize()"
    />

    <v-icon
        @click="addDialog = true"
    >
      mdi-plus
    </v-icon>

    <v-spacer></v-spacer>

    <v-btn color="neutral" @click="downloadStartList()">
      Startliste herunterladen
    </v-btn>

    <v-btn color="primary" @click="downloadResultList()">
      Ergebnisliste herunterladen
    </v-btn>


  </v-footer>


</template>

<script>

import RoundDialog from "@/components/round/RoundDialog.vue";
import TimerApiService from "@/plugins/timer-api";

export default {

  components: {RoundDialog},

  props: {
    rounds: {
      type: Array
    },
  },

  data: () => ({

    addDialog: false,

    selectedRoundId: null,

    editRound: {
      score: {
        holdType: null,
        holdNumber: null,
        tryNumber: null
      }
    }

  }),

  created() {
    this.initialize();
  },

  watch: {
    rounds: {
      handler() {
        this.selectedRoundId = this.rounds[0]?.id
      }
    },
    selectedRoundId: {
      handler() {
        this.$emit('update:selectedRoundId', this.selectedRoundId)
      }
    }

  },

  methods: {

    initialize() {

      this.editRound = {
        score: {
          holdType: null,
          holdNumber: null,
          tryNumber: null
        }
      }

      this.addDialog = false;

    },

    downloadStartList() {

      let round = this.rounds.find(round => round.id === this.selectedRoundId)

      let filename = 'startList-' + round.gender + '-' + round.roundNumber + '.pdf';

      TimerApiService.printStartList(this.selectedRoundId)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(() => {});

    },

    downloadResultList() {

      let round = this.rounds.find(round => round.id === this.selectedRoundId)

      let filename = 'resultList-' + round.gender + '.pdf';

      TimerApiService.printResultList(round.gender)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(() => {});

    },

  },

  computed: {

  }

}

</script>