<template>

  <div>

    <CompetitionHeader
        :rounds="rounds"
        @update:selectedRoundId="selectedRoundId = $event"
    />

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
            @updated="competitorRoundUpdated"
        ></CompetitorRoundCard>
      </v-col>

    </v-row>

  </div>


</template>

<script>

import CompetitorSimpleTable from "@/components/competitor/CompetitorSimpleTable.vue";
import CompetitorRoundCard from "@/components/competitorRound/CompetitorRoundCard.vue";
import CompetitionHeader from "@/components/competition/CompetitionHeader.vue";
import TimerApiService from "@/plugins/timer-api";


export default {

  components: {CompetitorRoundCard, CompetitionHeader, CompetitorSimpleTable},

  data: () => ({

    rounds: [],
    selectedRoundId: null,
    selectedRound: null,

    competitorRounds: [],
    selectedCompetitorRound: null,

  }),

  created() {
    this.initialize();
  },

  computed: {

  },

  watch: {

    selectedRoundId: function (newVal) {
      this.loadCompetitorRounds(newVal);
    },

  },

  methods: {

    initialize() {

      this.rounds = []
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
              element.roundName = element.gender + " " + element.roundNumber;
            }

            if (this.rounds.length > 0) {
              this.selectedRound = this.rounds[0];
            }

          })
          .catch(() => {});

    },

    loadCompetitorRounds(id) {

      if (!id) {
        return;
      }

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