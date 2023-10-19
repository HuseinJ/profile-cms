
<template>
  <h1>{{data.page.name}}</h1>
  <PageComponent v-for="component in data.page.pageComponents" :pageComponent="component"> test </PageComponent>
</template>

<script lang="ts" setup>
const query = gql`
  query getPage($name: String!){
    page(name: $name) {
      id
      name
      pageComponents {
        name
        componentData {
            key
            value
        }
      }
    }
  }
`
const route = useRoute();
const variables = { name: route.params.page }
const { data } = await useAsyncQuery(query, variables)

console.log(data.value.page.pageComponents)
</script>