
<template>
  <h1>{{data.page.name}}</h1>
</template>

<script lang="ts" setup>
const query = gql`
  query getPage($name: String!){
    page(name: $name) {
      id
      name
    }
  }
`
const route = useRoute();
const variables = { name: route.params.page }
const { data } = await useAsyncQuery(query, variables)
</script>