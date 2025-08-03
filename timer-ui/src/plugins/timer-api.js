
import axios from 'axios';

import {EventBus} from "@/plugins/event-bus";

const apiClient = axios.create({
    baseURL: window.env.VUE_APP_API_BASE_URL,
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

apiClient.interceptors.response.use(
    response => response,
    error => {

        EventBus.$emit('show-eror-snackbar', error.response.data.message);

        throw error;

    }
);

export default {

    ///////////////////////////////////////////////////////////////////////////////////
    // Common, Meta and Admin

    delete(type, id) {
        return apiClient.delete('/' + type + '/delete', {
            params: {id: id}
        });
    },

    deleteAllData() {
        return apiClient.delete('/admin/deleteAllData');
    },

    getStatistics() {
        return apiClient.get('/admin/getStatistics');
    },

    getEventTitle() {
        return apiClient.get('/meta/getEventTitle');
    },

    updateCurrentCompetitionRound(roundId) {
        return apiClient.post('/competition/updateCurrentRound', null, {
            params: {roundId: roundId}
        });
    },

    updateCurrentCompetitorRound(competitorRoundId) {
        return apiClient.post('/competition/updateCurrentCompetitorRound', null, {
            params: {competitorRoundId: competitorRoundId}
        });
    },

    getCurrentCompetitorRound() {
        return apiClient.get('/competition/getCurrentCompetitorRound');
    },

    getCurrentRound() {
        return apiClient.get('/competition/getCurrentRound');
    },

    getCurrentCompetitorRounds() {
        return apiClient.get('/competition/getCurrentCompetitorRounds');
    },

    getResultList(gender) {
        return apiClient.get('/competition/getResultList', {
            params: {gender: gender}
        });
    },

    getLatestCompetitorRounds(gender) {
        return apiClient.get('/competition/getLatestCompetitorRounds', {
            params: {gender: gender}
        });
    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Competitor

    createCompetitor(competitor) {
        return apiClient.post('/competitor/create', competitor);
    },

    updateOrCreateCompetitor(competitor) {

        let path = competitor.id ? '/competitor/update' : '/competitor/create';

        return apiClient.post(path, competitor);

    },

    getCompetitors() {
        return apiClient.get('/competitor/getCompetitors');
    },

    getStartNumber() {
        return apiClient.get('/competitor/generateStartNumber');
    },

    isStartNumberValid(competitor) {

        let queryParams = competitor.id
            ? {id: competitor.id, startNumber: competitor.startNumber}
            : {startNumber: competitor.startNumber};

        return apiClient.get('/competitor/isStartNumberValid', {
            params: queryParams
        });


    },

    getPossibleCompetitors() {
        return apiClient.get('/competitor/getPossibleCompetitors');
    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Competitor Rounds

    addCompetitorRound(competitorId, roundNumber) {

        return apiClient.post('/competitor-round/addCompetitorToRound', null, {
            headers: {'Content-Type': 'application/json'},
            params: {competitorId: competitorId, roundNumber: roundNumber}
        });

    },

    updateCompetitorRound(competitorRound) {
        return apiClient.post('/competitor-round/update',
            JSON.stringify(competitorRound),
            {
                headers: {'Content-Type': 'application/json'}
            });
    },

    getHoldTypes() {
        return apiClient.get('/competitor-round/getHoldTypes');
    },

    getCompetitorRounds(roundId) {

        return apiClient.get('/competitor-round/getCompetitorRounds', {
            params: {roundId: roundId}
        })
    },

    getAllCompetitorRounds() {
        return apiClient.get('/competitor-round/getAllCompetitorRounds');
    },

    deleteCompetitorRound(competitorRoundId) {
        return apiClient.delete('/competitor-round/delete', {
            params: {id: competitorRoundId}
        });
    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Print

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

    previewResultList(gender) {

        return apiClient.get('/print/' + 'previewResultList', {
            params: {gender: gender}
        });

    },

    resultListFromData(gender, data) {

        return apiClient.post('/print/' + 'resultListFromData', data, {
            responseType: 'blob',
            params: {gender: gender}
        });

    },

    ///////////////////////////////////////////////////////////////////////////////////
    // Round

    updateOrCreateRound(round) {

        let path = round.id ? '/round/update' : '/round/create';

        let queryParams = round.id ? {} : {addCompetitors: true};

        return apiClient.post(path, round, {
            params: queryParams
        });
    },

    previewRound(round) {
        return apiClient.post('/round/preview', round);
    },

    getRounds() {
        return apiClient.get('/round/getRounds');
    },

};
