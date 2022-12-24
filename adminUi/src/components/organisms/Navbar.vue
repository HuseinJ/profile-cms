<template>
  <div class="navigation-bar">
    <va-sidebar v-if="!isOnLoginPage" hoverable minimizedWidth="64px">

      <va-sidebar-item
          v-for="item in items"
          :key="item.title"
          :active="item.active"
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
import router from "@/router";

export default {
  name: "Navbar",
  data() {
    return {
      items: [
        {title: 'Home', icon: 'house', location: "/"},
      ],
    }
  },
  apollo: {},
  computed: {
    isOnLoginPage() {
      return this.$router.currentRoute.value.name === "login";
    }
  },
  methods: {
    logout() {
      localStorage.clear()
      console.log(this.$router);
      this.$router.push({name: "login"})
    },
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
</style>