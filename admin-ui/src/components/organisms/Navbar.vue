<template>
  <div class="navigation-bar">
    <div class="mobile-nav-bar" v-if="smAndDown">
      <div class="mobile-menu-logo">
        profile-cms-admin-ui
      </div>
      <div v-if="!isOnLoginPage" class="mobile-menu-open" @click="toggleMenu">
        <va-icon name="menu"/>
      </div>
    </div>
    <va-sidebar :model-value="sidebarOpen || mdAndUp" class="sidebar" v-if="!isOnLoginPage && (sidebarOpen || mdAndUp)" :minimized="mdAndUp" minimizedWidth="60px" width="100%">
      <va-sidebar-item
          v-for="item in items"
          :key="item.title"
          :active="item.active"
          @click="toggleMenu"
      >
        <router-link class="nav-link" :to="item.location">
          <va-sidebar-item-content>
            <va-icon :name="item.icon"/>

            <va-sidebar-item-title>
              {{ item.title }}
            </va-sidebar-item-title>
          </va-sidebar-item-content>
        </router-link>
      </va-sidebar-item>
      <va-sidebar-item
          key="logout"
          :active="false"
      >
        <va-sidebar-item-content @click="logout">
          <va-icon name="logout"/>
          <va-sidebar-item-title>
            Logout
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
    </va-sidebar>
  </div>
</template>

<script>
import BreakpointsMixin from "@/util/BreakpointsMixin";

export default {
  name: "Navbar",
  mixins: [BreakpointsMixin],
  data() {
    return {
      items: [
        {title: 'Home', icon: 'house', location: "/"},
        {title: 'Pages', icon: 'note', location: "pages"},
        {title: 'Account', icon: 'account_circle', location: "profile"},
      ],
      sidebarOpen: false,
    }
  },
  apollo: {},
  computed: {
    isOnLoginPage() {
      return this.$router.currentRoute.value.name === "login";
    },
  },
  methods: {
    logout() {
      localStorage.clear()
      this.$router.push({name: "login"})
    },
    toggleMenu() {
      this.sidebarOpen = !this.sidebarOpen;
    }
  },

}
</script>

<style lang="scss" scoped>

.navigation-bar {
  height: 100%;
}

.nav-link {
  text-decoration: none;
  color: $primary-color;
}

.mobile-nav-bar{
  width: 100%;
  height: $mobile-menu-bar-height;

  display: flex;
  align-items: center;

  background-color: $primary-color;

  .mobile-menu-open{
    margin-left: auto;
    margin-right: 20px;
  }

  .mobile-menu-logo{
    margin-left: 20px;
  }
}

.sidebar{
  position: fixed;
  top: $mobile-menu-bar-height;
  bottom: 0;
  left: 0;
  right: 0;
}

@media only screen and (min-width: $sm) {
  .sidebar{
    position: unset;
  }
}

</style>