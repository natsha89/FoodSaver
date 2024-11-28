import { createStore } from 'vuex';
import axios from 'axios';

// Sätt upp Axios bas-URL
axios.defaults.baseURL = 'http://localhost:8081'; // Byt till din backend-URL

export default createStore({
    state: {
        isAuthenticated: false,
        user: null,
        authToken: localStorage.getItem('authToken') || null,
        foodItems: [],
        recipes: [], // Ny state för recept
    },

    mutations: {
        setAuthenticated(state, status) {
            state.isAuthenticated = status;
        },
        setUser(state, user) {
            state.user = user;
        },
        setAuthToken(state, token) {
            state.authToken = token;
            localStorage.setItem('authToken', token); // Spara token i localStorage
        },
        clearUser(state) {
            state.user = null;
        },
        clearAuthToken(state) {
            state.authToken = null;
            localStorage.removeItem('authToken'); // Rensa token från localStorage
        },
        setFoodItems(state, items) {
            state.foodItems = items; // Uppdatera foodItems i state
        },
        setRecipes(state, recipes) {
            state.recipes = recipes;
        },
    },

    actions: {
        // Login-funktion
        async login({ commit }, credentials) {
            try {
                const response = await axios.post('/api/auth/login', credentials);
                if (response.data.success) {
                    commit('setAuthenticated', true);
                    commit('setUser', response.data.user);
                    commit('setAuthToken', response.data.token);
                }
            } catch (error) {
                console.error('Login failed:', error.response?.data?.message || error.message);
                throw new Error(error.response?.data?.message || 'Login failed.');
            }
        },

        // Logout-funktion
        async logout({ commit, state }) {
            try {
                await axios.post('/api/auth/logout', null, {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });

                commit('setAuthenticated', false);
                commit('clearUser');
                commit('clearAuthToken');
            } catch (error) {
                console.error('Logout failed:', error.response?.data?.message || error.message);
            }
        },

        // Hämta användarinformation
        async fetchUser({ commit, state }) {
            try {
                const response = await axios.get('/api/auth/user', {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('setUser', response.data);
            } catch (error) {
                console.error('Error fetching user data:', error.response?.data?.message || error.message);
                commit('setAuthenticated', false);
                commit('clearUser');
                commit('clearAuthToken');
            }
        },

        // Hämta matvaror
        async fetchFoodItems({ commit, state }) {
            try {
                const response = await axios.get(`/api/foodItems/user/${state.user.id}`, {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('setFoodItems', response.data);
            } catch (error) {
                console.error('Error fetching food items:', error.response?.data?.message || error.message);
            }
        },

        async fetchRecipes({ commit, state }) {
            try {
                const response = await axios.get(`/api/recipes/user/${state.user.id}`, {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('setRecipes', response.data);
            } catch (error) {
                console.error('Error fetching recipes:', error.response?.data?.message || error.message);
            }
        },
    },

    getters: {
        isAuthenticated(state) {
            return state.isAuthenticated;
        },
        user(state) {
            return state.user;
        },
        foodItems(state) {
            return state.foodItems; // Getter för att hämta matvaror
        },
    },
});

// Axios Request Interceptor
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Axios Response Interceptor
axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.error('Unauthorized - Logging out');
            const store = require('./index').default;
            store.dispatch('logout'); // Logga ut användaren automatiskt
        }
        return Promise.reject(error);
    }
);