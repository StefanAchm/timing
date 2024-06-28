<template>

  <v-app>

    <ErrorSnackbar
        :message="snackbar.message"
        :color="snackbar.color"
        :show.sync="snackbar.show"
        v-if="snackbar.show"
    />

    <v-navigation-drawer
        app
        :mini-variant.sync="mini"
        permanent
    >

      <template v-slot:append>
        <v-list-item class="px-2">

          <v-btn
              icon
              @click.stop="mini = !mini"
          >
            <v-icon v-if="!mini">mdi-chevron-left</v-icon>
            <v-icon v-if="mini">mdi-chevron-right</v-icon>

          </v-btn>

        </v-list-item>

      </template>

      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            Timer
          </v-list-item-title>
          <v-list-item-subtitle>

          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="item in items"
            :key="item.title"
            link @click="$router.push({ path: item.route})"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

    </v-navigation-drawer>


    <!-- Sizes your content based upon application components -->
    <v-main>

      <!-- Provides the application the proper gutter -->
      <v-container fluid>
        <router-view></router-view>
      </v-container>

    </v-main>

    <snackbar ref="snackbar"></snackbar>

<!--    <v-footer app>-->
<!--      &lt;!&ndash; &ndash;&gt;-->
<!--    </v-footer>-->

  </v-app>

</template>

<script>


import ErrorSnackbar from "@/components/ErrorSnackbar.vue";
import {EventBus} from "@/plugins/event-bus";
import snackbar from "@/components/SnackBar.vue";

export default {
  name: 'App',
  components: {snackbar, ErrorSnackbar},

  data() {
    return {
      items: [
        {title: 'Teilnehmer', icon: 'mdi-account-multiple-outline', route: '/competitors'},
        {title: 'Runden', icon: 'mdi-rotate-360', route: '/rounds'},
        {title: 'Teilnehmer Runden', icon: 'mdi-account-convert-outline', route: '/competitor-rounds'},
        {title: 'Veranstaltung', icon: 'mdi-scoreboard-outline', route: '/competition'},
      ],
      right: null,
      mini: true,
      snackbar: {
        show: false,
        message: '',
        color: 'error'
      }
    }

  },

  mounted() {
    this.$root.snackbar = this.$refs.snackbar;
  },

  created() {
    EventBus.$on('show-snackbar', (message) => {

      this.snackbar = {
        show: true,
        message: message,
        color: 'error'
      }
    })
  }


};
</script>
