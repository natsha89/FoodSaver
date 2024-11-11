// store.js

import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state: {
        isAuthenticated: false, // Track authentication state
        ingredients: [],
    },
    mutations: {
        setAuthenticated(state, status) {
            state.isAuthenticated = status; // Set the authentication status
        },
        setIngredients(state, ingredients) {
            state.ingredients = ingredients;
        },
        clearIngredients(state) {
            state.ingredients = [];
        },
    },
    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await axios.post('/api/auth/login', credentials);
                if (response.data.success) {
                    commit('setAuthenticated', true); // Set as authenticated
                }
            } catch (error) {
                console.error('Login failed:', error);
                commit('setAuthenticated', false);
            }
        },
        async logout({ commit }) {
            try {
                await axios.post('/api/auth/logout');
                commit('setAuthenticated', false); // Set as not authenticated
            } catch (error) {
                console.error('Logout failed:', error);
            }
        },
        // Fetch ingredients if needed
        async fetchIngredients({ commit }) {
            try {
                const response = await axios.get('/api/foodItems');
                commit('setIngredients', response.data);
            } catch (error) {
                console.error('Error fetching ingredients:', error);
            }
        },
    },
    getters: {
        isAuthenticated(state) {
            return state.isAuthenticated; // Return the current authentication status
        },
        allIngredients(state) {
            return state.ingredients;
        },
    },
});
