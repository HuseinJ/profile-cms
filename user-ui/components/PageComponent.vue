<template>
    <template v-if="pageComponent.name === 'PARAGRAPH'">
      <Paragraph :text="'lolol'" />
    </template>
    <template v-else-if="pageComponent.name === 'HEADING'">
      <Header :text="'lol head'" />
    </template>
    <template v-else>
      <!-- Handle other component types here -->
    </template>
</template>

<script>
import Paragraph from "~/components/atoms/Paragraph.vue";
import Header from "~/components/atoms/Header.vue";

export default {
  components: {Header, Paragraph},
  props: {
    pageComponent: Object // Pass the PageComponent as a prop
  },
  setup(props) {
    function getKeyValue(componentData, key) {
      console.log(componentData)
      const data = componentData.find(item => item.key === key);
      return data ? data.value : null;
    }

    return {
      getKeyValue,
    };
  }
};
</script>
