import axios from 'axios';

import {EventBus} from "@/plugins/event-bus";

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/timer/api',
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

apiClient.interceptors.response.use(
    response => response,
    error => {

        EventBus.$emit('show-snackbar', error.response.data.message);

        return {error};

    }
);

export default {

    ///

    delete(type, id) {
        return apiClient.delete('/' + type + '/delete', {
            params: {id: id}
        });
    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Competitor

    getCompetitors() {
        return apiClient.get('/competitor/getCompetitors');
    },

    getStartNumber() {
        return apiClient.get('/competitor/generateStartNumber');
    },

    // @deprecated
    createCompetitor(competitor) {
        return apiClient.post('/competitor/create', competitor);
    },

    getRounds() {
        return apiClient.get('/round/getRounds');
    },

    printStartList(id) {
        return apiClient.get('/print/' + 'startList', {
            responseType: 'blob',
            params: {id: id}
        });
    },

    printResultList(gender) {
        return apiClient.get('/print/' + 'resultList', {
            responseType: 'blob',
            params: {gender: gender}
        });
    },

    updateOrCreateCompetitor(competitor) {

        let path = competitor.id ? '/competitor/update' : '/competitor/create';
        return apiClient.post(path, competitor);

    },

    isStartNumberValid(competitor) {

        let queryParams = competitor.id
            ? {id: competitor.id, startNumber: competitor.startNumber}
            : {startNumber: competitor.startNumber};

        return apiClient.get('/competitor/isStartNumberValid', {
            params: queryParams
        });


    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Competitor Rounds


    getHoldTypes() {
        return apiClient.get('/competitor-round/getHoldTypes');
    },

    addCompetitorRound(competitorId, roundNumber) {

        return apiClient.post('/competitor-round/addCompetitorToRound', null, {
            headers: {'Content-Type': 'application/json'},
            params: {competitorId: competitorId, roundNumber: roundNumber}
        });

    },

    getCompetitorRounds(roundId) {

        return apiClient.get('/competitor-round/getCompetitorRounds', {
            params: {roundId: roundId}
        });
    },

    getAllCompetitorRounds() {

        return apiClient.get('/competitor-round/getAllCompetitorRounds');

    },

    updateCompetitorRound(competitorRound) {
        return apiClient.post('/competitor-round/update',
            JSON.stringify(competitorRound),
            {
                headers: {'Content-Type': 'application/json'}
            });
    },

    deleteCompetitorRound(competitorRoundId) {
        return apiClient.delete('/competitor-round/delete', {
            params: {id: competitorRoundId}
        });

    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Rounds

    previewRound(round) {
        return apiClient.post('/round/preview', round);
    },

    updateOrCreateRound(round) {

        let path = round.id ? '/round/update' : '/round/create';

        let queryParams = round.id ? {} : {addCompetitors: true};

        return apiClient.post(path, round, {
            params: queryParams
        });
    },

};
