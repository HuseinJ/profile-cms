<script setup>
import Header from "~/components/atoms/Header.vue";
import Paragraph from "~/components/atoms/Paragraph.vue";

const error = useError();

const handleError = () => clearError({ redirect: '/' })
</script>

<template>
  <div class="main-holder">

  <Header text="Whoops! Something went very wrong here :(" />
  <iframe allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen frameborder="0"
          height="200" src="https://www.youtube.com/embed/SiL2AjOtjZI?si=YAMJwmJX0xup5lCL"
          title="YouTube video player"
          width="300"></iframe>
  <Paragraph text="Maybe try turning it off and on again?"  />

  <h2>Status code is: {{ error.statusCode }}</h2>
  <Paragraph @click="handleError()" :text="'Either click here or show this Message to the page admin: ' + error.message" />
  </div>
</template>

<style lang="scss" scoped>
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