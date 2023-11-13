<template>
  <component v-if="data" v-for="(component, index) in data.home.pageComponents" :key="index" :is="componentName('PageComponent')" :pageComponent="component">
  </component>
</template>

<script lang="ts" setup>
import { defineAsyncComponent } from 'vue';

const componentName = (name: any)=> {
  const componentMap: any = {
    PageComponent: defineAsyncComponent(() => import('../components/PageComponent.vue')),
  };

  return componentMap[name] || 'div';
};

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
</script>