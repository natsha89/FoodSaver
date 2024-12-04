import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import VueToast from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-default.css';

// Create Vue application instance
const app = createApp(App);

// Use router, Vuex store, and Vuetify plugin
app.use(router);
app.use(store);
app.use(vuetify);
app.use(VueToast);

// Mount the app to the DOM
app.mount('#app');