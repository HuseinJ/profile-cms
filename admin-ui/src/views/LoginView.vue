<script setup lang="ts">
import {useMutation} from "@vue/apollo-composable";
import {LOGGED_IN_USER, LOGIN_MUTATION} from "@/queries/auth/login";
import {useRouter} from "vue-router";
import {ref} from "vue";
const router = useRouter();

const user = ref({
  name: '',
  password: ''
})

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
  router.push({name: "home"});
})

onError(() => {
  console.log("error")
})
</script>

<template>
  <main class="login-view">

    <div class="login-container">
      <va-form
          style="width: 300px;"
          tag="form"
          @submit.prevent=""
      >
        <va-input
            label="Username"
            v-model="user.name"
        />

        <va-input
            class="mt-3"
            type="password"
            label="Password"
            v-model="user.password"
        />

        <va-button type="submit" class="mt-3" @click="login({ name: user.name, password: user.password })">
          Login
        </va-button>
      </va-form>
    </div>

  </main>
</template>

<style lang="scss">
.login-view{
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;

  width: 100%;
}

.login-container{
  .va-form{
    display: flex;
    justify-content: center;
    align-items: center;

    flex-direction: column;
    .va-input-wrapper{
      margin-bottom: 20px;
    }
  }
}

@media only screen and (min-width: $sm) {
  .login-container{
    padding: 50px;
    border-radius: 20px;
    background-color: $primary-color;

    .va-button{width: 100%}
  }
}
</style>