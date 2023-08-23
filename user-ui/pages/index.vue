<template>
  <h1>Index of Page :)</h1>
  <p>something is happening here.. soon (tm)</p>
</template>

<script lang="ts" setup>
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