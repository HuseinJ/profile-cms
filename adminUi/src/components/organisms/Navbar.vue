<template>
  <div class="navigation-bar">
    <va-sidebar v-if="!isOnLoginPage" hoverable minimizedWidth="64px">
      <va-sidebar-item
          v-for="item in items"
          :key="item.title"
          :active="item.active"
      >
        <va-sidebar-item-content>
          <va-icon :name="item.icon" />
          <va-sidebar-item-title>
            {{ item.title }}
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
    </va-sidebar>
  </div>
</template>

<script>
export default {
  name: "Navbar",
  data () {
    return {
      items: [
        { title: 'Home', icon: 'house'},
      ],
    }
  },

  computed: {
    isOnLoginPage(){
      return this.$router.currentRoute.value.name == "login";
    }
  }
}
</script>

<style lang="scss" scoped>
  .navigation-bar{
    height: 100%;
  }
</style>