<script setup lang="ts">
import { RouterView, useRouter } from 'vue-router'
import Navbar from "@/components/organisms/Navbar.vue";
import { LOGGED_IN_USER } from "@/queries/auth/login";
import {useQuery} from "@vue/apollo-composable";
import {watch} from "vue";

const router = useRouter()
const { result, loading, error } = useQuery(LOGGED_IN_USER, null, {
  fetchPolicy: 'network-only'
})

watch(error, value => {
  router.push({
    name: "login"
  });
})

</script>

<template>
  <div class="main-container">
    <header>
      <Navbar></Navbar>
    </header>

    <RouterView />
  </div>

</template>

<style lang="scss">
  .main-container{
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  @media only screen and (min-width: $sm) {
    .main-container{
      display: flex;
      flex-direction: row;
      height: 100%;
    }
  }

</style>

