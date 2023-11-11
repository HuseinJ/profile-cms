// https://nuxt.com/docs/api/configuration/nuxt-config
import axios from 'axios'
async function getRoutes(apollo:any) {
  return axios({
    url: process.env.CMS_URL ? process.env.CMS_URL + '/graphql' : "localhost:8080/graphql",
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
        httpEndpoint: process.env.CMS_URL ? process.env.CMS_URL + '/graphql' : "localhost:8080/graphql",
      }
    },
  },
  hooks: {
    async 'nitro:config'(nitroConfig) {
      if (nitroConfig.dev) return
      let slugs = await getRoutes();
      nitroConfig.prerender.routes.push(...slugs);
    },
  },
  buildModules: [
    '@nuxtjs/style-resources'
  ],
  styleResources: {
    scss: [
      '@/assets/scss/main.scss', // Replace with your SCSS file path
    ],
    css: [
      '@fortawesome/fontawesome-svg-core/styles.css'
    ]
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
