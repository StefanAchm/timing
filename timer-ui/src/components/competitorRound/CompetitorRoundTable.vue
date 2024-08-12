<template>

  <div>

    <v-alert
        prominent
        type="warning"
        v-if="competitorsWithoutRounds.length > 0"
    >
      <v-row align="center">
        <v-col class="grow">
          Einige Teilnehmer ({{ competitorsWithoutRounds.length }}) haben noch
          keine Runden zugeordnet.
        </v-col>
        <v-col class="shrink">
          <v-btn @click="autoAdd()">Automatisch zuordnen</v-btn>
        </v-col>
      </v-row>
    </v-alert>

    <CompetitorRoundDialog
        :dialog.sync="competitorRoundDialog"
        :competitor-round="selectedCompetitorRound"
        @dialog-closed="init()"
    />

    <CompetitorDialog
        :competitor.sync="editedItem"
        :dialog.sync="dialogVisible"
        @dialog-closed="init()"
        :full-edit="true"/>

    <DeleteDialog
        :dialog.sync="dialogDelete"
        @dialog-closed="init()"
        :itemprop="editedItem"
        :type="'competitor'"
        :warning="deleteWarning"
    />

    <v-container>

      <v-row>

        <v-col :cols="4">

          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
          ></v-text-field>

        </v-col>

        <v-col :cols="2">

          <v-select
              label="Geschlecht"
              v-model="genderFilter"
              :items="['HERREN', 'DAMEN', 'ALLE']"
          ></v-select>

        </v-col>

        <v-col :cols="2">
          <v-select
              label="Runde"
              v-model="roundFilter"
              :items="nrOfRounds"
          ></v-select>
        </v-col>

        <v-col :cols="2">
          <v-select
              label="Status"
              v-model="statusFilter"
              :items="statusItems"
          ></v-select>
        </v-col>

        <v-col :cols="2">
          <v-switch
              v-model="roundView"
              :label="roundView ? 'Rundenansicht' : 'Teilnehmeransicht'"
          ></v-switch>
        </v-col>
      </v-row>


    </v-container>

    <v-data-table
        :headers="headers"
        :items="competitorsFiltered"
        :items-per-page="-1"
        :sort-by="['competitor.startNumber']"
    >

      <template v-slot:top>
        <v-toolbar flat>


          <v-spacer></v-spacer>

          <v-btn
              color="primary"
              dark
              class="mb-2"
              @click="dialogVisible = true"
          >TeilnehmerInn hinzufügen
          </v-btn>


        </v-toolbar>
      </template>

      <template v-slot:item="{ item }">

        <tr>

          <td>{{ item.competitor.startNumber }}</td>
          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td>{{ item.competitor.gender }}</td>

          <template v-if="roundView">

            <td
                v-for="i in maxNumberOfRounds()"
                :key="i">

            <span v-if="item.rounds[i]">


              {{ formatNumber(item.rounds[i].score) }}

              <v-icon
                  small
                  class="ml-2 mr-2"
                  @click="changeCompetitorRound(item.rounds[i].id)">
                mdi-pencil
              </v-icon>

              <v-icon
                  small
                  :disabled="hasNextRound(item, i)"
                  @click="deleteCompetitorRound(item.rounds[i].id)">
                mdi-delete
              </v-icon>


            </span>

              <v-btn
                  v-else
                  :disabled="previousRoundNotFinished(item, i)"
                  outlined
                  rounded
                  small
                  @click="addCompetitorToRound(item.competitor.id, i)">Hinzufügen
              </v-btn>


            </td>

            <td>{{ item.competitor.totalScore }}</td>

          </template>

          <template v-else>

            <td>{{ item.competitor.city }}</td>
            <td>{{ item.competitor.club }}</td>
            <td>{{ item.competitor.dateOfBirth }}</td>
            <td>{{ item.competitor.nrOfRounds }}</td> <!-- todo -->

            <td>

              <v-btn
                  v-if="item.competitor.paymentStatus === 'NOT_PAID'"
                  outlined
                  rounded
                  small
                  color="error"
                  @click="updateCompetitorPaymentStatus(item.competitor)">
                Bezahlen
              </v-btn>

              <v-chip
                  v-else
                  color="success"
              >
                {{ item.competitor.paymentStatus }}
              </v-chip>

            </td>


          </template>

          <td>
            <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
            <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
          </td>

        </tr>

      </template>


    </v-data-table>

  </div>

</template>

<script>

import TimerApi from "@/plugins/timer-api";
import CompetitorRoundDialog from "@/components/competitorRound/CompetitorRoundDialog.vue";
import DeleteDialog from "@/components/DeleteDialog.vue";
import CompetitorDialog from "@/components/competitor/CompetitorDialog.vue";
import TimerApiService from "@/plugins/timer-api";

export default {
  components: {CompetitorDialog, DeleteDialog, CompetitorRoundDialog},

  data: () => ({

    dialogDelete: false,
    deleteWarning: '',
    dialogVisible: false,
    editedItem: {},

    competitorRounds: [],
    competitors: [],
    headers: [],

    search: '',

    genderFilter: 'ALLE',
    roundFilter: 'ALLE',
    statusFilter: 'ALLE',

    competitorRoundDialog: false,
    selectedCompetitorRound: {},

    roundView: false,

  }),

  created() {
    this.init();
  },

  computed: {

    statusItems() {

      if (this.roundFilter === 'ALLE') {
        return ['ALLE', 'Bezahlt', 'Nicht bezahlt'];
      } else {
        return ['ALLE', 'Dabei', 'Nicht dabei', 'Abgeschlossen', 'Noch nicht gestartet', 'Bezahlt', 'Nicht bezahlt'];
      }

    },

    competitorsWithoutRounds() {
      console.log(this.competitors.filter(competitor => competitor.rounds[0]?.id === null))
      return this.competitors.filter(competitor => competitor.rounds[0]?.id === null);
    },

    competitorsFiltered() {

      let filteredCompetitors = this.competitors;

      if (this.genderFilter !== 'ALLE') {
        filteredCompetitors = filteredCompetitors
            .filter(competitor => competitor.competitor.gender === this.genderFilter);
      }

      if (this.roundFilter !== 'ALLE') {

        if (this.statusFilter === 'Dabei') {

          filteredCompetitors = filteredCompetitors
              .filter(competitor => competitor.rounds[this.roundFilter]);

        } else if (this.statusFilter === 'Nicht dabei') {

          // Filter all, which do not have a round of the selected round number

          filteredCompetitors = filteredCompetitors
              .filter(competitor => !competitor.rounds[this.roundFilter]);

        } else if (this.statusFilter === 'Abgeschlossen') {


          filteredCompetitors = filteredCompetitors
              .filter(competitor => competitor.rounds[this.roundFilter]?.competitorRoundStatus === 'COMPLETED');

        } else if (this.statusFilter === 'Noch nicht gestartet') {

          filteredCompetitors = filteredCompetitors
              .filter(competitor => competitor.rounds[this.roundFilter]?.competitorRoundStatus === 'CREATED');

        }

      }


      if (this.statusFilter === 'Bezahlt') {
        filteredCompetitors = filteredCompetitors
            .filter(competitor => competitor.competitor.paymentStatus === 'PAID')
      } else if (this.statusFilter === 'Nicht bezahlt') {
        filteredCompetitors = filteredCompetitors
            .filter(competitor => competitor.competitor.paymentStatus === 'NOT_PAID')
      }


      return filteredCompetitors.filter(item =>
              Object.values(item).some(value =>
                  value.toString().toLowerCase().includes(this.search.toLowerCase())
              ) || Object.values(item.competitor).some(value =>
                  value.toString().toLowerCase().includes(this.search.toLowerCase())
              )
      );


    },

    nrOfRounds() {
      let nrOfRounds = ['ALLE'];
      for (let i = 1; i <= this.maxNumberOfRounds(); i++) {
        nrOfRounds.push(i);
      }
      return nrOfRounds;
    }

  },

  watch: {
    roundView() {
      this.updateHeaders();
    },
  },

  methods: {

    init() {
      TimerApi.getAllCompetitorRounds()
          .then(response => {

            this.competitorRounds = response.data;

            this.updateHeaders();

            this.setCompetitorsArray();

          })
          .catch(() => {
          });
    },

    autoAdd() {

      let promises = [];

      for(const competitor of this.competitorsWithoutRounds) {

        promises.push(TimerApiService.addCompetitorRound(competitor.competitor.id, 1));

      }

      Promise.all(promises)
          .then(() => {
            this.$root.snackbar.showSuccess({message: promises.length + ' Runden zugeordnet'})
            this.init();
          })
          .catch(() => {});

    },

    formatNumber(value) {
      return value;

      // TODO
      // return value.toLocaleString('en-US', {
      //   minimumIntegerDigits: 3,
      //   minimumFractionDigits: 3,
      //   maximumFractionDigits: 3
      // });
    },

    hasNextRound(item, i) {
      return !!item.rounds[i + 1];
    },

    previousRoundNotFinished(item, i) {

      if (i === 1) {
        return false;
      }

      let previousRound = item.rounds[i - 1];

      if (previousRound === undefined) {
        return true;
      }

      return previousRound.competitorRoundStatus === 'CREATED';

    },

    maxNumberOfRounds() {

      let roundNumbers = this.competitorRounds
          .filter(competitorRound => competitorRound.roundNumber)
          .map(competitorRound => competitorRound.roundNumber);

      if(roundNumbers < 1) {
        return [];
      }

      return Math.max(...roundNumbers);

    },

    setCompetitorsArray() {

      this.competitors = [];
      this.competitorRounds.forEach(competitorRound => {

        let competitor = this.competitors.find(competitor => competitor.competitor.id === competitorRound.competitor.id);

        if (!competitor) {
          competitor = {competitor: competitorRound.competitor};
          this.competitors.push(competitor);
        }

        competitor.rounds = competitor.rounds || [];

        competitor.rounds[competitorRound.roundNumber] = {};

        competitor.rounds[competitorRound.roundNumber].id = competitorRound.id;
        competitor.rounds[competitorRound.roundNumber].competitorRoundStatus = competitorRound.competitorRoundStatus;


        if (competitorRound.competitorRoundStatus === 'COMPLETED') {
          competitor.rounds[competitorRound.roundNumber].score = competitorRound.score;
        } else if (competitorRound.competitorRoundStatus === 'CREATED') {
          competitor.rounds[competitorRound.roundNumber].score = 'DNS';
        }

      });

    },

    updateCompetitorPaymentStatus(item) {
      item.paymentStatus = 'PAID'
      TimerApi.updateOrCreateCompetitor(item)
          .then(() => {
            this.init();
          })
          .catch(() => {
          });
    },

    updateHeaders() {

      let maxNumberOfRounds = this.maxNumberOfRounds();

      this.headers = [
        {text: '#', value: 'competitor.startNumber'},
        {text: 'Name', value: 'competitor.firstName'},
        {text: 'Geschlecht', value: 'competitor.gender'}
      ];


      if (this.roundView) {

        for (let i = 1; i <= maxNumberOfRounds; i++) {
          this.headers.push({
            text: 'Runde ' + i,
            value: 'rounds[' + i + '].score',
            sortable: true
          });
        }

        this.headers.push({text: 'Punkte', value: 'competitor.totalScore'})

      } else {

        this.headers.push({text: 'Stadt', value: 'competitor.city'})
        this.headers.push({text: 'Verein', value: 'competitor.club'})
        this.headers.push({text: 'Geburtsdatum', value: 'competitor.dateOfBirth'})
        this.headers.push({text: 'Runden', value: 'nrOfRounds'})
        this.headers.push({text: 'Status', value: 'competitor.paymentStatus'})

      }


      this.headers.push({text: 'Aktionen', value: 'actions', sortable: false})

    },

    addCompetitorToRound(competitorId, roundNumber) {

      TimerApi.addCompetitorRound(competitorId, roundNumber)
          .then(() => {
            this.init();
          })
          .catch(() => {
          });
    },

    changeCompetitorRound(competitorRoundId) {

      this.selectedCompetitorRound = this.competitorRounds.find(competitorRound => competitorRound.id === competitorRoundId);
      this.competitorRoundDialog = true;

    },

    editItem(item) {
      this.editedItem = Object.assign({}, item.competitor)
      this.dialogVisible = true
    },

    deleteItem(item) {
      this.editedItem = Object.assign({}, item.competitor)
      this.dialogDelete = true

      let completedRounds = item.rounds.filter(round => round.competitorRoundStatus === 'COMPLETED');

      if (completedRounds.length > 0) {
        this.deleteWarning = 'Achtung: Der Teilnehmer hat bereits Runden zugeordnet.'
      } else {
        this.deleteWarning = ''
      }

    },

    deleteCompetitorRound(competitorRoundId) {

      TimerApi.deleteCompetitorRound(competitorRoundId)
          .then(() => {
            this.$root.snackbar.showWarning({message: 'Gelöscht'})
            this.init();
          })
          .catch(() => {
          });

    },

  }

}

</script>