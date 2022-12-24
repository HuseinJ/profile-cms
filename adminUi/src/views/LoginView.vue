<script setup lang="ts">
import {useMutation} from "@vue/apollo-composable";
import {LOGGED_IN_USER, LOGIN_MUTATION} from "@/queries/auth/login";

const {mutate: login, onDone, onError} = useMutation(LOGIN_MUTATION,
    () => ({
      update: (cache, {data: {signIn}}) => {
        localStorage.setItem('access-token', signIn.token);
        const data = {loggedInUser: {...signIn.user}}
        cache.writeQuery({query: LOGGED_IN_USER, data })
      },
    }))

onDone(() => {
  console.log("done")
})

onError(() => {
  console.log("error")
})
</script>

<template>
  <main>
    <button @click="login({ name: 'admin', password: 'supersecureadminpassword' })">
      Send message
    </button>
  </main>
</template>