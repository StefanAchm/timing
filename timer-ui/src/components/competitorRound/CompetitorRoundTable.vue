<template>

  <div>

    <CompetitorRoundDialog
        :dialog.sync="competitorRoundDialog"
        :competitor-round="selectedCompetitorRound"
        @dialog-closed="init"
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
    >

      <template v-slot:item="{ item }">
        <tr>

          <td>{{ item.competitor.firstName }} {{ item.competitor.lastName }}</td>
          <td>{{ item.competitor.gender }}</td>

          <td v-for="i in maxNumberOfRounds()" :key="i">

          <span v-if="item['round' + i]">

            {{ item['round' + i].score }}

            <v-icon small class="ml-2 mr-2" @click="changeCompetitorRound(item['round' + i].id)">mdi-pencil</v-icon>
            <v-icon small @click="deleteCompetitorRound(item['round' + i].id)">mdi-delete</v-icon>


          </span>

            <v-btn
                v-else
                outlined
                rounded
                small
                @click="addCompetitorToRound(item.competitor.id, i)">Hinzufügen
            </v-btn>


          </td>

          <td> {{item.competitor.totalScore}}</td>

        </tr>

      </template>


    </v-data-table>

  </div>

</template>

<script>

import TimerApi from "@/plugins/timer-api";
import CompetitorRoundDialog from "@/components/competitorRound/CompetitorRoundDialog.vue";

export default {
  components: {CompetitorRoundDialog},

  data: () => ({

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
      if(this.roundFilter !== 'ALLE') {

        if(this.statusFilter === 'Dabei') {

            filteredCompetitors = filteredCompetitors
                .filter(competitor => competitor['round' + this.roundFilter]);

        } else if(this.statusFilter === 'Nicht dabei') {

          // Filter all, which do not have a round of the selected round number

          filteredCompetitors = filteredCompetitors
              .filter(competitor => !competitor['round' + this.roundFilter]);

        } else if(this.statusFilter === 'Abgeschlossen') {


          filteredCompetitors = filteredCompetitors
              .filter(competitor => competitor['round' + this.roundFilter]?.competitorRoundStatus === 'COMPLETED');

        } else if(this.statusFilter === 'Noch nicht gestartet') {

          filteredCompetitors = filteredCompetitors
              .filter(competitor => competitor['round' + this.roundFilter]?.competitorRoundStatus === 'CREATED');

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
    },

    maxNumberOfRounds() {

      let roundNumbers = this.competitorRounds
          .filter(competitorRound => competitorRound.roundNumber)
          .map(competitorRound => competitorRound.roundNumber);

      return Math.max(...roundNumbers);

    },

    setCompetitorsArray() {

      // Create an array with objects, like: {competitor, round1, round2, round3, ...}

      this.competitors = [];

      this.competitorRounds.forEach(competitorRound => {

        let competitor = this.competitors.find(competitor => competitor.competitor.id === competitorRound.competitor.id);

        if (!competitor) {
          competitor = {competitor: competitorRound.competitor};
          this.competitors.push(competitor);
        }

        competitor['round' + competitorRound.roundNumber] = {};

        competitor['round' + competitorRound.roundNumber].id = competitorRound.id;
        competitor['round' + competitorRound.roundNumber].competitorRoundStatus = competitorRound.competitorRoundStatus;


        if (competitorRound.competitorRoundStatus === 'COMPLETED') {
          competitor['round' + competitorRound.roundNumber].score = competitorRound.score;
        } else {
          competitor['round' + competitorRound.roundNumber].score = 'DNS';
        }

      });

    },

    updateHeaders() {

      let maxNumberOfRounds = this.maxNumberOfRounds();

      this.headers = [
        {text: 'Name', value: 'competitor.firstName'},
        {text: 'Gender', value: 'competitor.gender'}
      ];

      for (let i = 1; i <= maxNumberOfRounds; i++) {
        this.headers.push({text: 'Runde ' + i, value: 'round' + i});
      }

      this.headers.push({text: 'Punkte', value: 'competitor.totalScore'})

    },

    addCompetitorToRound(competitorId, roundNumber) {

      TimerApi.addCompetitorRound(competitorId, roundNumber)
          .then(() => {

            this.init();

          })
    },

    changeCompetitorRound(competitorRoundId) {

      this.selectedCompetitorRound = this.competitorRounds.find(competitorRound => competitorRound.id === competitorRoundId);
      this.competitorRoundDialog = true;

    },

    deleteCompetitorRound(competitorRoundId) {

      TimerApi.deleteCompetitorRound(competitorRoundId)
          .then(() => {
            this.$root.snackbar.showWarning({message: 'Gelöscht'})
            this.init();
          })

    },

  }

}

</script>