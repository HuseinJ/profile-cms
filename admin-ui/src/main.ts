import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import './assets/main.scss'
import { createVuesticEssential, VaButton, VaSidebar, VaSidebarItem, VaSidebarItemContent, VaSidebarItemTitle, VaIcon, VaConfig, VaForm, VaInput, VaNavbar, VaNavbarItem } from 'vuestic-ui'
import 'vuestic-ui/styles/essential.css'

import { ApolloClient } from 'apollo-client'
import { createHttpLink } from 'apollo-link-http'
import { InMemoryCache } from 'apollo-cache-inmemory'
import VueApollo from 'vue-apollo'
import {DefaultApolloClient} from "@vue/apollo-composable";

console.log("port:", import.meta.env.VITE_BASE_GRAPHQL_API_PORT)

const httpLink = createHttpLink({
    uri: import.meta.env.VITE_BASE_API_URL + ":" + import.meta.env.VITE_BASE_GRAPHQL_API_PORT + import.meta.env.VITE_BASE_GRAPHQL_PATH,
    fetch: (uri: RequestInfo, options: RequestInit) => {
        options.headers = getHeaders();
        return fetch(uri, options);
    },
})

function getHeaders() {
    const headers: { Authorization?: string; "Content-Type"?: string } = {};
    const token = localStorage.getItem("access-token");
    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }
    headers["Content-Type"] = "application/json";
    return headers;
}

const cache = new InMemoryCache()

const apolloClient = new ApolloClient({
    link: httpLink,
    cache,
})

const apolloProvider = new VueApollo({
    defaultClient: apolloClient,
})

const app = createApp(App).provide(DefaultApolloClient, apolloClient).use(router)
    .use(createVuesticEssential({ components: {VaButton, VaSidebar, VaSidebarItem, VaSidebarItemContent, VaSidebarItemTitle, VaIcon, VaConfig, VaForm, VaInput, VaNavbar, VaNavbarItem} }));
app.mount('#app')
