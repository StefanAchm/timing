<template>
  <v-dialog v-model="dialog" max-width="800px" persistent>
    <v-card>
      <v-card-title class="text-h5 pa-4">
        <v-icon class="mr-2" color="primary">mdi-clipboard-text</v-icon>
        Regeln <br> K.I.O.T. Bouldercup
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-4" style="max-height: 600px; overflow-y: auto;">
        <div class="text-body-2">
          <!-- Teilnahmebedingungen -->
          <div class="mb-8">
            <div class="mb-4">
              <h3 class="text-h6 mb-2 text-primary font-weight-bold">1. Teilnahmeberechtigung</h3>
              <p class="mb-0 text-body-2">Die Teilnahme am Wettkampf steht allen Personen offen. Minderjährige (unter 18 Jahren) benötigen für die Teilnahme am K.I.O.T Bouldercup das Einverständnis der Erziehungsberechtigten.</p>
            </div>

            <div class="mb-4">
              <h3 class="text-h6 mb-2 text-primary font-weight-bold">2. Anmeldung</h3>
              <p class="mb-0 text-body-2">Die Anmeldung erfolgt über dieses Formular. Mit der Anmeldung erklärt sich der Teilnehmer mit den Wettkampfregeln einverstanden.</p>
            </div>

            <div class="mb-4">
              <h3 class="text-h6 mb-2 text-primary font-weight-bold">3. Datenschutz</h3>
              <p class="mb-0 text-body-2">Die angegebenen Daten werden ausschließlich für die Durchführung des Wettkampfs verwendet und nicht an Dritte weitergegeben.</p>
            </div>

            <div class="mb-4">
              <h3 class="text-h6 mb-2 text-primary font-weight-bold">4. Haftung</h3>
              <p class="mb-0 text-body-2">Die Teilnahme erfolgt auf eigene Gefahr. Der Veranstalter übernimmt keine Haftung für Schäden jeder Art.</p>
            </div>
          </div>

          <v-divider class="my-6"></v-divider>

          <!-- Wettkampfregeln -->
          <div>
            <h3 class="text-h6 mb-4 text-primary font-weight-bold d-flex align-center">
              <v-icon class="mr-2" small>mdi-gavel</v-icon>
              Wettkampfregeln
            </h3>

            <v-alert type="warning" outlined class="mb-6">
              <div class="font-weight-bold mb-1">Wichtig:</div>
              <div class="text-body-2">Die Entscheidung des Präsidenten des Vereines K.I.O.T. steht über jeder Regel. Eine Regeländerung zur Belustigung der Zuseher ist also jederzeit durch den Präsidenten des Vereins K.I.O.T. möglich.</div>
            </v-alert>

            <div class="mb-6">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                Punktesystem
              </h4>
              <p class="mb-0 text-body-2">Die Punkteanzahl einer Runde setzt sich aus erreichtem Griff, Griffsituation und benötigte Versuche zusammen.</p>
            </div>

            <div class="mb-6">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                Qualifikation
              </h4>
              <p class="mb-0 text-body-2">Für das Erreichen der nächsten Runde wird eine Mindestpunkteanzahl benötigt (Diese wird von den Routenbauern festgelegt).</p>
            </div>

            <div class="mb-6">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                Kletterregeln
              </h4>
              <p class="mb-0 text-body-2">Beim Klettern dürfen nur die vorgesehenen Griffe und Tritte verwendet werden. Das zur Hilfe nehmen der Strukturwand ist erlaubt, jedoch darf weder die baulichen Fugen noch die Kanten der Wand als Tritt oder Griff benutzt werden. Sollte dies der Fall sein wird der Versuch als ungültig bewertet.</p>
            </div>

            <div class="mb-6">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                Routenbauer
              </h4>
              <div class="text-body-2">
                <p class="mb-2">Die Einzelheiten der jeweiligen Routen werden vor Beginn des Durchgangs von den Routenbauern besprochen. Diese bestimmen auch ob es sich bei der Runde um eine sogenannte „offene" Runde handelt oder ob sich die Kletterer in der „Isozone" einfinden müssen.</p>
                <p class="mb-0 font-weight-medium">Den Routenbauern ist in jedem Fall Folge zu leisten, diese haben bei kurzfristigen Entscheidungen die gleiche Obrigkeit wie der Präsident des Vereines K.I.O.T.</p>
              </div>
            </div>

            <div class="mb-0">
              <h4 class="text-subtitle-1 font-weight-bold mb-2 text-primary">
                Einsprüche
              </h4>
              <p class="mb-3 text-body-2">Einsprüche gegen eine Entscheidung werden nur bis zum Start des nächsten Durchganges entgegengenommen.</p>

              <v-alert type="info" outlined dense>
                <div class="font-weight-bold mb-1">Besondere Regelung:</div>
                <div class="text-body-2">Der Einspruch wird nur gegen Bezahlung einer Getränkerunde an den Präsidenten, die Routenbauer, die Judges, DJs sowie den Platzsprecher ordnungsgemäß behandelt.</div>
              </v-alert>
            </div>
          </div>
        </div>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn
            color="primary"
            variant="elevated"
            @click="acceptRules"
        >
          <v-icon class="mr-2">mdi-check</v-icon>
          Verstanden
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'TermsDialog',

  props: {
    modelValue: {
      type: Boolean,
      default: false
    }
  },

  emits: ['update:modelValue', 'rules-accepted', 'rules-closed'],

  computed: {
    dialog: {
      get() {
        return this.modelValue
      },
      set(value) {
        this.$emit('update:modelValue', value)
      }
    }
  },

  methods: {
    closeDialog() {
      this.dialog = false
      this.$emit('rules-closed')
    },

    acceptRules() {
      this.dialog = false
      this.$emit('rules-accepted')
    }
  }
}
</script>

<style scoped>
.rules-section {
  line-height: 1.6;
}

.rule-item {
  border-left: 3px solid rgb(var(--v-theme-primary));
  padding-left: 16px;
}

.v-card-text {
  font-size: 14px;
}

h3.text-primary {
  color: rgb(var(--v-theme-primary)) !important;
}
</style>