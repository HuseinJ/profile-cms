<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import Navbar from "@/components/organisms/Navbar.vue";
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
    flex-direction: row;
    height: 100%;
  }

</style>

