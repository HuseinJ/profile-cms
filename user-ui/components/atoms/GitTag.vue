<script lang="ts">
import {defineComponent} from 'vue'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import Paragraph from "~/components/atoms/Paragraph.vue";

export default defineComponent({
  name: "GitTag",
  components: {Paragraph, FontAwesomeIcon},
  props:{
    url: {
      type: String,
      required: true
    },
    tagText: {
      type: String,
      required: true
    }
  }
})
</script>

<template>
  <a :href="url" target="_blank">
    <div class="git-tag">
      <FontAwesomeIcon icon="fa-brands fa-github-square" />
      <Paragraph v-if="tagText" :text="tagText" />
    </div>
  </a>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss";

a{
  display: block;
  color: $primaryBright;
}
.git-tag{
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