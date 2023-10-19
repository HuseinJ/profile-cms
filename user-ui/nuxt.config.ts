// https://nuxt.com/docs/api/configuration/nuxt-config
import axios from 'axios'
async function getRoutes(apollo:any) {
  return axios({
    url: 'http://api.testing.hjusic.com:8081' + '/graphql',
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
        httpEndpoint: 'http://api.testing.hjusic.com:8081' + '/graphql'
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
})
