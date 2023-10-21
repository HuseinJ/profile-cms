// https://nuxt.com/docs/api/configuration/nuxt-config
import axios from 'axios'
async function getRoutes(apollo:any) {
  return axios({
    url: process.env.CMS_URL + '/graphql',
    method: 'post',
    data: {
      query: `
                query {
                  pages{
                    name
                  }
                }
            `,
    },
  }).then((result) => {
    return result.data.data.pages.map((page) => {
      return '/' + page.name
    })
  })
}
export default defineNuxtConfig({
  modules: ['@nuxtjs/apollo'],
  apollo: {
    clients: {
      default: {
        httpEndpoint: process.env.CMS_URL + '/graphql'
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
  buildModules: [
    '@nuxtjs/style-resources'
  ],
  styleResources: {
    scss: [
      '@/assets/scss/main.scss', // Replace with your SCSS file path
    ],
  },
  loaders: {
    sass: {
      implementation: require('sass'),
    },
    scss: {
      implementation: require('sass'),
    },
  },
})
