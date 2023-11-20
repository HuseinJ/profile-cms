<script setup lang="ts">
import Paragraph from "~/components/atoms/Paragraph.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

import {defineProps} from "vue";

const props = defineProps(['mailto', 'tagText']);
</script>

<template>
  <a :href="'mailto:' + mailto" target="_blank">
    <div class="email-tag">
      <FontAwesomeIcon icon="fa-solid fa-envelope" />
      <Paragraph v-if="tagText" :text="tagText" />
    </div>
  </a>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss";

a{
  display: block;
  color: inherit;
}
.email-tag{
  height: $baseHeight;
  max-height: $baseHeight;
  display: flex;
  flex-direction: row;

  justify-content: center;
  align-items: center;

  cursor: pointer;

  svg{
    height: inherit;
    margin-right: $baseMargin;
  }
}
</style>