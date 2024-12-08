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
        notifications: [],
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
        updateUser(state, user) {
            state.user = { ...state.user, ...user };
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
        setRecipes(state, recipes) {
            state.recipes = recipes;
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

        async fetchUser({ commit, state }) {
            try {
                const response = await axios.get('/api/users/me', {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });

                // Extract the actual user data from the response
                // Assuming the response structure is { data: { message: '...', data: { user } } }
                const userData = response.data.data;

                commit('setUser', userData); // Update user data in Vuex store
                return userData;
            } catch (error) {
                console.error('Error fetching user:', error.response?.data?.message || error.message);
                throw error; // Throw to be handled in the component
            }
        },
        async updateUser({ commit, state }, updatedUser) {
            try {
                const response = await axios.put('/api/users/me', updatedUser, {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('updateUser', response.data); // Uppdatera användaren i Vuex store
                return response.data;
            } catch (error) {
                console.error('Error updating user:', error.response?.data?.message || error.message);
                throw error;
            }
        },
        async deleteUser({ commit, state }) {
            try {
                await axios.delete('/api/users/me', {
                    headers: { Authorization: `Bearer ${state.authToken}` },
                });
                commit('clearUser'); // Remove user data from Vuex
                commit('setAuthenticated', false); // Log out the user
                commit('clearAuthToken'); // Clear auth token
                localStorage.removeItem('authToken'); // Remove token from localStorage
                // Optionally, you could redirect to the login page or home page after deletion
                return true; // Return a success status
            } catch (error) {
                console.error('Error deleting user:', error.response?.data?.message || error.message);
                throw error; // Rethrow error to be handled in the component
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
                const updatedRecipes = state.recipes.filter(recipe => recipe.id !== recipeId);
                commit('setRecipes', updatedRecipes);

                return true;
            } catch (error) {
                console.error('Error deleting recipe:', error.response?.data?.message || error.message);
                throw error; // Rethrow to allow component to handle
            }
        },
        async generateRecipe({ commit, state }, recipeData) {
            try {
                const response = await axios.post('/api/recipes/generate', {
                    ingredients: recipeData.ingredients,
                    allergens: recipeData.allergens || [],
                    dietaryPreferences: recipeData.dietaryPreferences || '',
                    servings: recipeData.servings || 1
                }, {
                    headers: { Authorization: `Bearer ${state.authToken}` }
                });

                commit('setRecipes', response.data);
                return response.data;
            } catch (error) {
                console.error('Error creating recipe:', error.response?.data?.message || error.message);
                throw error;
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
        async createFoodItem({ commit, state }, foodItem) {
            try {
                const response = await axios.post('/api/foodItems', foodItem, {
                    headers: { Authorization: `Bearer ${state.authToken}` }
                });

                // Commit the new food item to the state
                commit('setFoodItems', response.data);

                return response.data;
            } catch (error) {
                console.error('Error creating food item:', error.response?.data?.message || error.message);
                throw error;
            }
        },

        async updateFoodItem({ commit, state }, { id, foodItem }) {
            try {
                const response = await axios.put(`/api/foodItems/${id}`, foodItem, {
                    headers: { Authorization: `Bearer ${state.authToken}` }
                });

                // Update the food items in the state
                commit('setFoodItems', response.data);

                return response.data;
            } catch (error) {
                console.error('Error updating food item:', error.response?.data?.message || error.message);
                throw error;
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