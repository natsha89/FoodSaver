/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

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