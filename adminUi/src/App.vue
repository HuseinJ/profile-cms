<script setup lang="ts">
import { RouterView, useRouter } from 'vue-router'
import Navbar from "@/components/organisms/Navbar.vue";
import { LOGGED_IN_USER } from "@/queries/auth/login";
import {useQuery} from "@vue/apollo-composable";
import {watch} from "vue";

const router = useRouter()
const { result, loading, error } = useQuery(LOGGED_IN_USER)

watch(error, value => {
  router.push({
    name: "login"
  });
})

</script>

<template>
  <div v-if="loading">Loading...</div>
  <div v-else-if="error">Error: {{ error.message }}</div>
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
    flex-direction: row;
    height: 100%;
  }

</style>

