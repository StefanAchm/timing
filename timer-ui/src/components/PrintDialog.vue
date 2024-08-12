<template>
  <v-dialog
      v-model="dialogVisible"
      max-width="400"
  >
    <v-card>
      <v-card-title class="headline">
        Drucken
      </v-card-title>
      <v-card-text>
        <v-container>

          <v-row>
            <v-col>
              <v-select
                  label="Typ"
                  :items="['Startliste', 'Ergebnisliste']"
                  v-model="printType"
              >
              </v-select>
            </v-col>
          </v-row>

          <v-row>
            <v-col v-if="printType=='Startliste'">
              <v-select
                  label="Runde"
                  :items="rounds"
                  item-text="roundName"
                  item-value="id"
                  v-model="selectedRoundId"
              >
              </v-select>
            </v-col>
            <v-col v-else>
              <v-select
                  label="Geschlecht"
                  :items="['HERREN', 'DAMEN']"
                  v-model="selectedGender"
              >
              </v-select>
            </v-col>
          </v-row>

        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="dialogVisible = false">Abbrechen</v-btn>
        <v-btn color="blue darken-1" text @click="printType==='Startliste' ? downloadStartList() : downloadResultList()">Drucken</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import TimerApiService from "@/plugins/timer-api";

export default {

  props: {
    dialog: Boolean
  },

  data: () => ({
    printType: 'Startliste',
    rounds: [],
    selectedRoundId: null,
    selectedGender: 'HERREN'
  }),

  created() {
    this.init();
  },

  methods: {

    init() {
      this.loadRounds();
    },

    loadRounds() {
      TimerApiService.getRounds()
          .then(response => {

            this.rounds = response.data

            for (const element of this.rounds) {
              element.roundName = element.roundNumber + ' - ' + element.gender + '';
            }

            // sort by roundNumber

            this.rounds.sort((a, b) => {
              return a.roundNumber - b.roundNumber;
            });

            if (this.rounds.length > 0) {
              this.selectedRoundId = this.rounds[0].id;
            }

            console.log(this.rounds)

          })
          .catch(() => {
          });

    },

    downloadStartList() {

      let filename = 'Startliste-' + this.selectedRound.gender + '-Runde' + this.selectedRound.roundNumber + '.pdf';

      console.log(this.selectedRound.id)

      TimerApiService.printStartList(this.selectedRound.id)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(() => {
          });

    },

    downloadResultList() {

      let filename = 'Ergebnisliste-' + this.selectedGender + '.pdf';

      TimerApiService.printResultList(this.selectedGender)
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();
          })
          .catch(() => {
          });

    },
  },

  computed: {

    selectedRound() {
      return this.rounds.find(round => round.id === this.selectedRoundId);
    },

    dialogVisible: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },
  }
}
</script>