import { createApp } from 'vue'; // Importera createApp från vue
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify'; // Importera Vuetify

const app = createApp(App); // Skapa en Vue-applikation
app.use(router); // Använd router
app.use(store); // Använd Vuex store
app.use(vuetify); // Använd Vuetify
app.mount('#app'); // Montera applikationen på DOM
