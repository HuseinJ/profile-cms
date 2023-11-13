
<template>
  <div v-if="data.page">
    <h1>{{data.page.name}}</h1>
    <PageComponent v-for="component in data.page.pageComponents" :pageComponent="component"> test </PageComponent>
  </div>
  <E404 v-else/>

</template>

<script lang="ts" setup>

import E404 from "~/components/molecule/E404.vue";

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
</script>