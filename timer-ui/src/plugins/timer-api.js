
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

// JWT Token management
const getAuthToken = () => {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    return user ? user.token : null;
};

const setAuthToken = (token) => {
    if (token) {
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        delete apiClient.defaults.headers.common['Authorization'];
    }
};

// Set token on app initialization
const token = getAuthToken();
if (token) {
    setAuthToken(token);
}

// Request interceptor to add token to requests
apiClient.interceptors.request.use(
    config => {
        const token = getAuthToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Response interceptor for error handling and token expiration
apiClient.interceptors.response.use(
    response => response,
    error => {
        // Handle token expiration
        if (error.response && error.response.status === 401) {
            // Clear stored user data
            localStorage.removeItem('user');
            delete apiClient.defaults.headers.common['Authorization'];

            EventBus.$emit('show-error-snackbar', 'Session expired. Please login again.');
        } else if (error.response && error.response.data && error.response.data.message) {
            EventBus.$emit('show-error-snackbar', error.response.data.message);
        }

        throw error;
    }
);

export default {

    ///////////////////////////////////////////////////////////////////////////////////
    // Authentication

    login(username, password) {
        return apiClient.post('/auth/login', {
            username,
            password
        }).then(response => {
            if (response.data.token) {
                const userData = {
                    token: response.data.token,
                    username: response.data.username,
                    role: response.data.role
                };
                localStorage.setItem('user', JSON.stringify(userData));
                setAuthToken(userData.token);
                return userData;
            }
            return response.data;
        });
    },

    logout() {
        localStorage.removeItem('user');
        setAuthToken(null);
    },

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            try {
                return JSON.parse(userStr);
            } catch (e) {
                localStorage.removeItem('user');
                return null;
            }
        }
        return null;
    },


    isAuthenticated() {

        const user = this.getCurrentUser()
        return !!(user && user.token)

    },

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

    updateCompetition(roundId, competitorRoundId) {
        return apiClient.post('/competition/update', null, {
            params: {roundId: roundId, competitorRoundId: competitorRoundId}
        });
    },

    updateLiveStatus(status) {
        return apiClient.post('/competition/updateLiveStatus', null, {params: {live: status}});
    },

    getLiveStatus() {
        return apiClient.get('/competition/getLiveStatus');
    },

    updateRegistrationStatus(status) {
        return apiClient.post('/competition/updateRegistrationStatus', null, {params: {registrationOpen: status}});
    },

    getRegistrationStatus() {
        return apiClient.get('/competition/getRegistrationStatus');
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

    register(competitor) {
        return apiClient.post('/competition/register', competitor);
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
