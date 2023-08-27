<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Header",
  props:{
    text: {
      type: String,
      required: true
    }
  }
})
</script>

<template>
<h1>{{text}}</h1>
</template>

<style scoped lang="scss">

</style>