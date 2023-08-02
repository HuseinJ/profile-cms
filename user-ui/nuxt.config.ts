// https://nuxt.com/docs/api/configuration/nuxt-config
import { gql } from '@apollo/client/core';

async function getRoutes(apollo:any) {
  const { data } = await apollo.query({
    query: gql`
      query {
        pages{
          name
        }
      }
    `
  });
  return data.pages.map((page) => (`/${page.name}`));
}

export default defineNuxtConfig({
  modules: ['@nuxtjs/apollo'],
  apollo: {
    clients: {
      default: {
        httpEndpoint: 'http://api.testing.hjusic.com:8080' + '/graphql'
      }
    },
  },
  hooks: {
    async 'nitro:config'(nitroConfig) {
      if (nitroConfig.dev) return

      let slugs = await getRoutes();
      nitroConfig.prerender.routes.push(...slugs);
      return
    },
  },
})
