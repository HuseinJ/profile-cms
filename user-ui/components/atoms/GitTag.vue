<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "GitTag",
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
      <font-awesome-icon icon="fa-brands fa-github-square" />
      <span v-if="tagText && tagText != ''">{{tagText}}</span>
    </div>
  </a>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss";

a{
  display: block;
  color: inherit;
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