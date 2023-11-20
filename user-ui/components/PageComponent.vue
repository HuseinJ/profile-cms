<template>
    <template v-if="pageComponent.name === 'PARAGRAPH'">
      <Paragraph :text="getValueFromKey(pageComponent.componentData,'text')"  />
    </template>
    <template v-else-if="pageComponent.name === 'HEADER'">
      <Header :text="getValueFromKey(pageComponent.componentData,'text')" />
    </template>
  <template v-else-if="pageComponent.name === 'GIT_TAG'">
    <GitTag :url="getValueFromKey(pageComponent.componentData,'url')" :tag-text="getValueFromKey(pageComponent.componentData,'tag-text')" />
  </template>
  <template v-else-if="pageComponent.name === 'IMAGE'">
    <Image :img-src="getValueFromKey(pageComponent.componentData,'src')" :alt-text="getValueFromKey(pageComponent.componentData,'alt')" />
  </template>
  <template v-else-if="pageComponent.name === 'MAIL_TAG'">
    <EmailTag :mailto="getValueFromKey(pageComponent.componentData,'mailTo')" :tag-text="getValueFromKey(pageComponent.componentData,'tag-text')" />
  </template>
  <template v-else-if="pageComponent.name === 'LINKEDIN_TAG'">
    <LinkedInTag :url="getValueFromKey(pageComponent.componentData,'url')" :tag-text="getValueFromKey(pageComponent.componentData,'tag-text')" />
  </template>
    <template v-else>
      <!-- Handle other component types here -->
    </template>
</template>

<script>
import Paragraph from "~/components/atoms/Paragraph.vue";
import Header from "~/components/atoms/Header.vue";
import GitTag from "~/components/atoms/GitTag.vue";
import Image from "~/components/atoms/Image.vue";
import EmailTag from "~/components/atoms/EmailTag.vue";
import LinkedInTag from "~/components/atoms/LinkedInTag.vue";

export default {
  components: {LinkedInTag, EmailTag, Image, GitTag, Header, Paragraph},
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
  },
  methods: {
    getValueFromKey(componentData, targetKey) {
      for (let i = 0; i < componentData.length; i++) {
        if (componentData[i].key === targetKey) {
          return componentData[i].value;
        }
      }
      return null; // If the key is not found in the array
    }
  }
};
</script>
