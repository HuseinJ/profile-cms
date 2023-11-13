<script setup lang="ts">

import Paragraph from "~/components/atoms/Paragraph.vue";
import Header from "~/components/atoms/Header.vue";
</script>

<template>
  <title>404 Page not found :(</title>
  <Header text="Whoops! 404 - The page could not be found" />
  <iframe allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen frameborder="0"
          height="200" src="https://www.youtube.com/embed/SiL2AjOtjZI?si=YAMJwmJX0xup5lCL"
          title="YouTube video player"
          width="300"></iframe>
  <Paragraph text="Maybe try turning it off and on again?"  />
</template>