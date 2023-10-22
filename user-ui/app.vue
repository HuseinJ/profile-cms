<template>
  <div class="main-holder">
    <NuxtLayout>
      <NuxtPage/>
    </NuxtLayout>
    <Footer></Footer>
  </div>
</template>

<script lang="ts" setup>
import Footer from "~/components/atoms/Footer.vue";
</script>

<style scoped lang="scss">
@import "@/assets/scss/main.scss"; // Import the SCSS file

.main-holder{
  margin: $baseMargin;

  display: flex;
  flex-direction: column;

  width: calc(100% - $baseMargin - $baseMargin);

  @include respond-to('large') {
    max-width: $large;
    width: $large;
    padding: 0;
  }
}
</style>

<style lang ="scss">
@import "@/assets/scss/main.scss"; // Import the SCSS file
$default-font: 'RobotoMono', sans-serif; // Use the font-family you defined in @font-face

h1, h2, h3, h4, h5, h6 {
  font-family: $default-font;
}

p {
  font-family: $default-font;
}

span {
  font-family: $default-font;
}

body{
  margin: 0;

  background-color: antiquewhite;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}


html{
  height: 100vh;
}
</style>
