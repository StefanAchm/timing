import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  render: h => h(App),
}).$mount('#app')

Vue.use(vuetify);

// Vue.prototype.$errorHandler = function (message) {
//
//   console.error('asdf' + message);
//
//   console.log(this.$vuetify);
//
//   // This.$vuetify is undefined, because it is not a Vue instance
//   //
//
//   this.$vuetify.snackbar = {
//     show: true,
//     color: 'error',
//     message: message,
//   };
//
// };