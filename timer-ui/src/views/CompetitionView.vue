<template>

  <div>

    <ApplicationHeader @update="init"/>

    <v-row>
      <v-col :cols="4" align-self="center"/>
      <v-col class="pb-0" :cols="4" align-self="center">

        <v-select
            v-model="selectedRoundId"
            :items="rounds"
            item-text="roundName"
            item-value="id"
            label="Runde"
            outlined
        ></v-select>

      </v-col>
      <v-col :cols="4" align-self="center"/>
    </v-row>

    <v-row>

      <v-col>
        <CompetitorSimpleTable
            :selectedRound="selectedRound"
            :selectedCompetitorRound.sync="selectedCompetitorRound"
            :competitor-rounds="competitorRounds"
        />

      </v-col>

      <v-col>
        <CompetitorRoundCard
            :competitor-round="selectedCompetitorRound"
            :max-number-of-holds="selectedRound?.maxHolds"
            @updated="competitorRoundUpdated"
        ></CompetitorRoundCard>
      </v-col>

    </v-row>

  </div>


</template>

<script>

import CompetitorSimpleTable from "@/components/competitor/CompetitorSimpleTable.vue";
import CompetitorRoundCard from "@/components/competitorRound/CompetitorRoundCard.vue";
import TimerApiService from "@/plugins/timer-api";
import ApplicationHeader from "@/components/ApplicationHeader.vue";


export default {

  components: {ApplicationHeader, CompetitorRoundCard, CompetitorSimpleTable},

  data: () => ({

    rounds: [],
    selectedRoundId: null,
    selectedRound: null,

    competitorRounds: [],
    selectedCompetitorRound: null,

  }),

  created() {
    this.init();
  },

  computed: {

  },

  watch: {

    selectedRoundId: function (newVal) {
      this.loadCompetitorRounds(newVal);
    },

  },

  methods: {

    init() {

      this.rounds = []
      this.selectedRoundId = null;
      this.selectedRound = null;

      this.competitorRounds = [];
      this.selectedCompetitorRound = null;

      this.loadRounds();
      this.loadCompetitorRounds();

    },

    competitorRoundUpdated() {

      // choose the next competitorRound:
      let indexOfCurrent = this.competitorRounds.findIndex(competitorRound => competitorRound.id === this.selectedCompetitorRound.id);

      let newIndex = indexOfCurrent + 1;

      for(let i = newIndex; i < this.competitorRounds.length; i++) {
        if (this.competitorRounds[i].competitorRoundStatus === 'CREATED') {
          this.selectedCompetitorRound = this.competitorRounds[i];
          return;
        }
      }

      for (let i = 0; i < indexOfCurrent; i++) {
        if (this.competitorRounds[i].competitorRoundStatus === 'CREATED') {
          this.selectedCompetitorRound = this.competitorRounds[i];
          return;
        }
      }

      this.selectedCompetitorRound = null;

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
              this.selectedRound = this.rounds[0];
              this.selectedRoundId = this.rounds[0].id;
            }

          })
          .catch(() => {});

    },

    loadCompetitorRounds(id) {

      if (!id) {
        return;
      }

      this.selectedCompetitorRound = null;
      this.selectedRound = this.rounds.find(round => round.id === id);

      TimerApiService.getCompetitorRounds(id)
          .then(response => {
            this.competitorRounds = response.data;

            // Sort list
            this.competitorRounds.sort((a, b) => {
              return a.competitor.startNumber - b.competitor.startNumber;
            });

          })
          .catch(() => {});

    },

  }

}
</script>