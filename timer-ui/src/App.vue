<template>

  <v-app>


    <v-bottom-navigation
        app
        grow
        v-if="$route.meta.mobile"
        color="primary"
        background-color="white"
        elevation="8"
        height="70"
        class="bottom-nav-enhanced"
    >
<!--      <v-btn-->
<!--          :value="'live'"-->
<!--          @click="$router.push('/live')"-->
<!--          class="nav-btn"-->
<!--          :class="{ 'active-btn': $route.path === '/live' }"-->
<!--      >-->
<!--        <span class="nav-text">Live</span>-->
<!--        <v-icon size="24" class="nav-icon">mdi-play-circle-outline</v-icon>-->
<!--      </v-btn>-->

      <v-btn
          :value="'live'"
          @click="$router.push('/live')"
          class="nav-btn"
          :class="{ 'active-btn': $route.path === '/live' }"
      >
        <span class="nav-text">Live</span>
        <v-icon size="24" class="nav-icon">mdi-play-circle-outline</v-icon>
      </v-btn>

      <v-btn
          :value="'liveStart'"
          @click="$router.push('/liveStart')"
          class="nav-btn"
          :class="{ 'active-btn': $route.path === '/liveStart' }"
      >
        <span class="nav-text">Startlisten</span>
        <v-icon size="24" class="nav-icon">mdi-format-list-bulleted</v-icon>
      </v-btn>

      <v-btn
          :value="'liveResults'"
          @click="$router.push('/liveResults')"
          class="nav-btn"
          :class="{ 'active-btn': $route.path === '/liveResults' }"
      >
        <span class="nav-text">Ergebnisse</span>
        <v-icon size="24" class="nav-icon">mdi-podium</v-icon>
      </v-btn>




    </v-bottom-navigation>

    <v-navigation-drawer
        app
        :mini-variant.sync="mini"
        permanent
        v-else-if="$route.name !== 'Login'"

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

      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="item in items"
            :key="item.title"
            link
            @click.stop="$router.push({ path: item.route})"
        >

          <v-tooltip bottom v-if="mini">
            <template v-slot:activator="{ on, attrs }">
              <v-list-item-icon v-bind="attrs" v-on="on">
                <v-icon>{{ item.icon }}</v-icon>
              </v-list-item-icon>
            </template>
            <span>{{ item.title }}</span>
          </v-tooltip>

          <v-list-item-icon v-if="!mini">
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-divider></v-divider>


        <v-list-item
            link
            @click.stop="handleLogout"
        >
          <v-list-item-icon>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>Logout</v-list-item-title>
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

import {EventBus} from "@/plugins/event-bus";
import snackbar from "@/components/SnackBar.vue";
import TimerApi from "@/plugins/timer-api";

export default {
  name: 'App',
  components: {snackbar},

  data() {
    return {
      items: [
        {title: 'Teilnehmer', icon: 'mdi-account-multiple-outline', route: '/competitor-rounds'},
        {title: 'Runden', icon: 'mdi-rotate-360', route: '/rounds'},
        {title: 'Veranstaltung', icon: 'mdi-scoreboard-outline', route: '/competition'},
        {title: 'Admin', icon: 'mdi-cog-outline', route: '/admin'},

      ],
      right: null,
      mini: true,
    }

  },

  mounted() {
    this.$root.snackbar = this.$refs.snackbar;
  },

  created() {
    EventBus.$on('show-eror-snackbar', (message) => {
      this.$root.snackbar.showError({message: message})
    })

  },

  methods: {
    handleLogout() {
      TimerApi.logout();
      this.$router.push('/login');

    },
  },

};
</script>


<style scoped>
.bottom-nav-enhanced {
  border-top: 1px solid rgba(0, 0, 0, 0.12);
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1) !important;
}

.nav-btn {
  flex-direction: column !important;
  height: 100% !important;
  min-width: 80px !important;
  padding: 8px 12px !important;
  border-radius: 0 !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  position: relative;
}

.nav-btn:hover {
  background-color: rgba(25, 118, 210, 0.08) !important;
  transform: translateY(-2px);
}

.nav-btn.active-btn {
  background-color: rgba(25, 118, 210, 0.12) !important;
  color: #1976d2 !important;
}

.nav-btn.active-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 32px;
  height: 3px;
  background-color: #1976d2;
  border-radius: 0 0 3px 3px;
}

.nav-text {
  font-size: 12px !important;
  font-weight: 500 !important;
  margin-bottom: 4px !important;
  line-height: 1.2 !important;
  text-transform: capitalize !important;
  letter-spacing: 0.5px !important;
}

.nav-icon {
  opacity: 0.8;
  transition: all 0.3s ease !important;
}

.nav-btn:hover .nav-icon,
.nav-btn.active-btn .nav-icon {
  opacity: 1;
  transform: scale(1.1);
}

.nav-btn.active-btn .nav-text {
  font-weight: 600 !important;
}

/* Ripple effect enhancement */
.nav-btn .v-ripple__container {
  border-radius: 8px !important;
}

/* Mobile responsiveness */
@media (max-width: 360px) {
  .nav-text {
    font-size: 10px !important;
  }

  .nav-icon {
    font-size: 20px !important;
  }

  .nav-btn {
    min-width: 60px !important;
    padding: 6px 8px !important;
  }
}
</style>