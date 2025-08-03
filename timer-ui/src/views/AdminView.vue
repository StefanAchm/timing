<template>
  <v-container class="admin-view py-8" fluid>

    <v-row justify="center" class="mb-4">
      <v-col cols="12" md="8" lg="10" class="text-center">
        <v-sheet color="grey lighten-4" rounded class="pa-4">
          <div class="subtitle-1 font-weight-bold">Angemeldeter Benutzer:</div>
          <div class="display-1 font-weight-bold">{{ currentUser().username }}</div>
        </v-sheet>
      </v-col>
    </v-row>

    <v-row justify="center" class="mb-8">
      <v-col cols="12" md="8" lg="10">
        <v-card elevation="8" class="pa-4">
          <v-card-title class="headline font-weight-bold text-center">
            <v-icon color="primary" left>mdi-chart-bar</v-icon>
            Statistiken
          </v-card-title>
          <v-divider class="mb-4"></v-divider>
          <v-card-text>
            <v-row>
              <v-col cols="12" sm="4" class="text-center">
                <v-sheet color="blue lighten-4" rounded class="pa-4">
                  <div class="subtitle-1 font-weight-bold">Teilnehmer</div>
                  <div class="display-1 font-weight-bold">{{ stats.competitorCount }}</div>
                </v-sheet>
              </v-col>
              <v-col cols="12" sm="4" class="text-center">
                <v-sheet color="green lighten-4" rounded class="pa-4">
                  <div class="subtitle-1 font-weight-bold">Runden</div>
                  <div class="display-1 font-weight-bold">{{ stats.roundCount }}</div>
                </v-sheet>
              </v-col>
              <v-col cols="12" sm="4" class="text-center">
                <v-sheet color="amber lighten-4" rounded class="pa-4">
                  <div class="subtitle-1 font-weight-bold">Durchgänge</div>
                  <div class="display-1 font-weight-bold">{{ stats.competitorRoundCount }}</div>
                </v-sheet>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12" md="8" lg="10">
        <v-card elevation="6" class="pa-4">
          <v-card-title class="headline font-weight-bold text-center">
            <v-icon color="error" left>mdi-delete</v-icon>
            Alle Daten löschen
          </v-card-title>
          <v-divider class="mb-4"></v-divider>
          <v-card-text>
            <v-btn
                :disabled="stats.roundCount === 0 && stats.competitorCount === 0 && stats.competitorRoundCount === 0"
                color="error" large block @click="openDialog">
              <v-icon left>mdi-alert</v-icon>
              Alle Daten löschen
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="showDialog" max-width="500">
      <v-card>
        <v-card-title class="headline font-weight-bold">
          <v-icon color="error" left>mdi-alert</v-icon>
          Daten löschen bestätigen
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text>
          <p>
            Geben Sie <strong>DELETE</strong> ein, um alle Daten <span
              class="red--text font-weight-bold">unwiderruflich</span> zu löschen:
          </p>
          <v-text-field
              v-model="confirmText"
              label="Bestätigung"
              placeholder="DELETE"
              outlined
              dense
              color="error"
              autofocus
          />
          <v-alert v-if="errorMessage" type="error" dense>{{ errorMessage }}</v-alert>
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn color="error" :disabled="confirmText !== 'DELETE'" @click="deleteAll">
            <v-icon left>mdi-check</v-icon>
            Bestätigen
          </v-btn>
          <v-btn text @click="closeDialog">
            <v-icon left>mdi-close</v-icon>
            Abbrechen
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import TimerApiService from "@/plugins/timer-api";
import TimerApi from "@/plugins/timer-api";

export default {
  data() {
    return {
      showDialog: false,
      confirmText: '',
      errorMessage: '',
      stats: {
        roundCount: 0,
        competitorCount: 0,
        competitorRoundCount: 0
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
    openDialog() {
      this.showDialog = true;
      this.confirmText = '';
      this.errorMessage = '';
    },
    closeDialog() {
      this.showDialog = false;
      this.confirmText = '';
      this.errorMessage = '';
    },
    init() {
      TimerApiService.getStatistics()
          .then(response => {
            this.stats = response.data;
          })
          .catch(() => {
            this.$root.snackbar.showError({message: 'Fehler beim Laden der Statistiken.'});
          });
    },
    currentUser() {
      return TimerApi.getCurrentUser();
    },
    async deleteAll() {

      TimerApiService.deleteAllData()
          .then(() => {
            this.$root.snackbar.showSuccess({message: 'Daten erfolgreich gelöscht.'});
            this.closeDialog();
            this.init(); // Refresh statistics after deletion
          })
          .catch((error) => {
            if (error.response && error.response.status === 403) {
              this.errorMessage = 'Sie haben keine Berechtigung, diese Aktion auszuführen.';
            } else {
              this.errorMessage = 'Beim Löschen der Daten ist ein Fehler aufgetreten.';
            }

          });

    }
  }
};
</script>

<style scoped>

</style>