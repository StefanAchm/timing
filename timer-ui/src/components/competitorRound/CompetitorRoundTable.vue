<template>

  <div>

    <CompetitorRoundDialog
        :dialog.sync="competitorRoundDialog"
        :competitor-round="selectedCompetitorRound"
        @dialog-closed="init"
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

        <v-col>

          <v-select
              label="Geschlecht"
              v-model="genderFilter"
              :items="['HERREN', 'DAMEN', 'ALLE']"
          ></v-select>

        </v-col>

        <v-col>
          <v-select
              label="Runde"
              v-model="roundFilter"
              :items="nrOfRounds"
          ></v-select>
        </v-col>

        <v-col>
          <v-select
              :disabled="roundFilter === 'ALLE'"
              label="Status"
              v-model="statusFilter"
              :items="['Dabei', 'Nicht dabei', 'Abgeschlossen', 'Noch nicht gestartet']"
          ></v-select>
        </v-col>

      </v-row>

    </v-container>

    <v-data-table
        :headers="headers"
        :items="competitorsFiltered"
        :items-per-page="-1"
        :sort-by="['competitor.startNumber']"
    >

      <template v-slot:item="{ item }">

        <tr>

          <td>{{ item.competitor.startNumber }}</td>
          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td>{{ item.competitor.gender }}</td>

          <td v-for="i in maxNumberOfRounds()" :key="i">

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
    statusFilter: 'Dabei',

    competitorRoundDialog: false,
    selectedCompetitorRound: {}

  }),

  created() {
    this.init();
  },

  computed: {

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

      if(i === 1) {
        return false;
      }

      let previousRound = item.rounds[i - 1];

      if(previousRound === undefined) {
        return true;
      }

      return previousRound.competitorRoundStatus === 'CREATED';

    },

    maxNumberOfRounds() {

      let roundNumbers = this.competitorRounds
          .filter(competitorRound => competitorRound.roundNumber)
          .map(competitorRound => competitorRound.roundNumber);

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

    updateHeaders() {

      let maxNumberOfRounds = this.maxNumberOfRounds();

      this.headers = [
        {text: '#', value: 'competitor.startNumber'},
        {text: 'Name', value: 'competitor.firstName'},
        {text: 'Gender', value: 'competitor.gender'}
      ];

      for (let i = 1; i <= maxNumberOfRounds; i++) {
        this.headers.push({
              text: 'Runde ' + i,
              value: 'rounds[' + i + '].score',
              sortable: true
            });
      }

      this.headers.push({text: 'Punkte', value: 'competitor.totalScore'})

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

      if(completedRounds.length > 0) {
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