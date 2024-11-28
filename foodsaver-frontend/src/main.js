import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify'; // Vuetify setup

// Create Vue application instance
const app = createApp(App);

// Use router, Vuex store, and Vuetify plugin
app.use(router);
app.use(store);
app.use(vuetify);

// Mount the app to the DOM
app.mount('#app');