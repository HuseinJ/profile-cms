<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps(['imgSrc', 'altText']);
</script>

<template>
  <div class="image-container">
    <img :src="imgSrc" :alt="altText" />
  </div>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss"; // Import the SCSS file

.image-container{
  display: flex;
  justify-content: center;
  align-items: center;

  height: $imgSmall;

  @include respond-to('medium') {
    height: $imgMedium;
  }

  @include respond-to('large') {
    height: $imgBig;
  }

  img{
    max-width: 100%;
    max-height: 100%;
  }
}

</style>