// https://nuxt.com/docs/api/configuration/nuxt-config
import { gql } from '@apollo/client/core';

export default defineNuxtConfig({
  modules: ['@nuxtjs/apollo'],
  apollo: {
    clients: {
      default: {
        httpEndpoint: process.env.BASE_API_URL + '/graphql'
      }
    },
  },
  async generate() {
    // Step 1: Fetch data from GraphQL to generate dynamic routes
    const { data } = await this.$apollo.query({
      query: gql`
        query {
          pages{
            id
            name
            pageComponents {
              id
            }
          }
        }
      `
    });

    // Step 2: Map the data to create dynamic routes array
    const dynamicRoutes = data.pages.map((page) => ({
      route: `/${page.name}`,
      payload: page // You can optionally pass data as payload
    }));

    return dynamicRoutes;
  },
})
