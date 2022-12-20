import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import './assets/main.scss'
import { createVuesticEssential, VaButton, VaSidebar, VaSidebarItem, VaSidebarItemContent, VaSidebarItemTitle, VaIcon } from 'vuestic-ui'
import 'vuestic-ui/styles/essential.css'

const app = createApp(App)

app.use(router)
app.use(createVuesticEssential({ components: {VaButton, VaSidebar, VaSidebarItem, VaSidebarItemContent, VaSidebarItemTitle, VaIcon} }));
app.mount('#app')
