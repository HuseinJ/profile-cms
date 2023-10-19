<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Paragraph",
  props:{
    text: {
      type: String,
      required: true
    }
  }
})
</script>

<template>
<p>{{text}}</p>
</template>

<style scoped lang="scss">
  p{
    hyphens: auto;
    text-align: justify
  }
</style>