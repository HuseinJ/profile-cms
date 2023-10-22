<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Notice"
})
</script>

<template>
<div class="notice">some content in notice</div>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss";
  .notice{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    width: calc(100% - $baseMargin - $baseMargin);
    background-color: blue;
  }
</style>