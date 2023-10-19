<template>
  <PageComponent v-for="component in data.home.pageComponents" :pageComponent="component"> test </PageComponent>
</template>

<script lang="ts" setup>
import Header from "~/components/atoms/Header.vue";
import Paragraph from "~/components/atoms/Paragraph.vue";
import SubHeader from "~/components/atoms/SubHeader.vue";

const query = gql`
  query Home{
    home {
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
const { data } = await useAsyncQuery(query)

console.log(data)
</script>