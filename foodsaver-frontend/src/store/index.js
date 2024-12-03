import { createStore } from 'vuex';
import axios from 'axios';

// Sätt upp Axios bas-URL
axios.defaults.baseURL = 'http://localhost:8081'; // Byt till din backend-URL

export default createStore({
    state: {
        isAuthenticated: false, // Användarens inloggningsstatus
        user: null, // Information om den inloggade användaren
        authToken: localStorage.getItem('authToken') || null, // Token lagrad i localStorage
        foodItems: [], // Ny state för att lagra matvaror
        recipes: [],
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
        removeFoodItems(state, foodItemId) {
            state.foodItems = state.foodItems.filter(foodItem => foodItem.id !== foodItemId);
        },
        setRecipes(state, items) {
            state.recipes = items;
        },
        removeRecipe(state, recipeId) {
            state.recipes = state.recipes.filter(recipe => recipe.id !== recipeId);
        },
    },

    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await axios.post('/api/auth/login', credentials);
                const token = response.data.data.token;

                if (token) {
                    commit('setAuthToken', token);
                    commit('setAuthenticated', true);
                    commit('setUser', response.data.data.user);
                } else {
                    throw new Error('No token received');
                }
            } catch (error) {
                console.error('Login Error:', error);
                throw error;
            }
        },

        // Logout-funktion
        async logout({ commit }) {
            try {
                await axios.post('/api/auth/logout', null, {
                    headers: { Authorization: `Bearer ${this.state.authToken}` },
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
        async fetchRecipes({ commit, state }) {
            try {
                const response = await axios.get('/api/recipes', {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('setRecipes', response.data); // Uppdatera state med matvarorna
            } catch (error) {
                console.error('Error fetching recipes:', error.response?.data?.message || error.message);
            }
        },
        async deleteRecipe({ commit, state }, recipeId) {
            try {
                await axios.delete(`/api/recipes/${recipeId}`, {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('removeRecipe', recipeId);
            } catch (error) {
                console.error('Error deleting recipe:', error.response?.data?.message || error.message);
                throw error; // Rethrow to allow component to handle
            }
        },

        // Hämta matvaror
        async fetchFoodItems({ commit, state }) {
            try {
                const response = await axios.get('/api/foodItems', {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('setFoodItems', response.data); // Uppdatera state med matvarorna
            } catch (error) {
                console.error('Error fetching food items:', error.response?.data?.message || error.message);
            }
        },

        // Delete a food item
        async deleteFoodItem({ commit, state }, foodItemId) {
            try {
                await axios.delete(`/api/foodItems/${foodItemId}`, {
                    headers: { Authorization: `Bearer ${state.authToken}` }
                });

                // Optionally, you can remove the item from the local state
                const updatedFoodItems = state.foodItems.filter(item => item.id !== foodItemId);
                commit('setFoodItems', updatedFoodItems);

                return true;
            } catch (error) {
                console.error('Error deleting food item:', error.response?.data?.message || error.message);
                throw error;
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
        recipes(state) {
            return state.recipes;
        },
    },
});

// Axios Request Interceptor
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            // Remove 'Bearer ' prefix if already present in stored token
            config.headers['Authorization'] = token.startsWith('Bearer ')
                ? token
                : `Bearer ${token}`;
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