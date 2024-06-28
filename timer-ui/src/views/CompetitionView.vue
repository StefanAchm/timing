<template>

  <div>

    <CompetitionHeader
        :selectedRoundId.sync="selectedRoundId"
    />

    <v-row>

      <v-col>
        <CompetitorSimpleTable
            :roundId="selectedRoundId"
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

    selectedRoundId: null,
    selectedCompetitorRound: null,

    competitorRounds: []

  }),

  created() {
    this.initialize();
  },

  watch: {
    selectedRoundId: function (newVal) {
      this.loadCompetitorRounds(newVal);
      // this.selectedCompetitorRoundLocal = null;
    },

  },

  methods: {

    initialize() {
      this.selectedRoundId = null;
      this.selectedCompetitorRound = null;
      this.competitorRounds = [];
      this.loadCompetitorRounds(this.selectedRoundId);
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

          });
    },

  }

}
</script>