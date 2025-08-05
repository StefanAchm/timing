import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
    icons: {
        iconfont: 'mdi',
    },
    theme: {
        options: {
            customProperties: true,
        },
        themes: {
            light: {

                // Navigation bar, major buttons, active states, footers
                primary: '#00416a',

                // Secondary buttons, hover states, highlights
                secondary: '#819595',

                // CUSTOM COLORS:

                greener: '#4caf50',

                firstPlace: '#FFA000', // Gold
                secondPlace: '#78909C', // Silver
                thirdPlace: '#8D6E63', // Bronze

                boys: '#e3f2fd', // Blue
                girls: '#fce4ec', // Pink

                // Background, card colors, text
                neutral: '#F2F2F3',
                neutral2: '#C8CDD0',

                // Status colors
                error: '#D41111',
                warning: '#FFAB1A',
                info: '#2196F3',
                success: '#73C322',

            },
        },
    },
});
