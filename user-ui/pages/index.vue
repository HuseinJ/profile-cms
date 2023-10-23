<template>
  <Header text="Under Construction"></Header>
  <Paragraph text="This is powered by my own CMS which currently is still in development. For more information, visit my GitHub repository" />
  <GitTag url="https://github.com/HuseinJ/profile-cms" tag-text="Profile-CMS" />
  <PageComponent v-if="data" v-for="component in data.home.pageComponents" :pageComponent="component"> test </PageComponent>
</template>

<script lang="ts" setup>
import Header from "~/components/atoms/Header.vue";
import Paragraph from "~/components/atoms/Paragraph.vue";
import GitTag from "~/components/atoms/GitTag.vue";

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