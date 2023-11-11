<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "SubHeader",
  props:{
    text: {
      type: String,
      required: true
    }
  }
})
</script>

<template>
  <h2>{{text}}</h2>
</template>