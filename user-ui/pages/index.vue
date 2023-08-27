<template>
  <Header
  text="Header 1"
  />
  <Paragraph text="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." />
</template>

<script lang="ts" setup>
import Header from "~/components/atoms/Header.vue";
import Paragraph from "~/components/atoms/Paragraph.vue";

const query = gql`
  query Home{
    home {
      id
      name
    }
  }
`
const { data } = await useAsyncQuery(query)
</script>