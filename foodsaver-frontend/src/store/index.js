import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state: {
        isAuthenticated: false, // Initially, the user is not logged in
        user: null, // Store user information after login
    },

    mutations: {
        setAuthenticated(state, status) {
            state.isAuthenticated = status; // Set the authentication status
        },

        setUser(state, user) {
            state.user = user; // Set user data after successful login
        },

        clearUser(state) {
            state.user = null; // Clear user data on logout
        },
    },

    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await axios.post('/api/auth/login', credentials);
                if (response.data.success) {
                    commit('setAuthenticated', true); // User logged in successfully
                    commit('setUser', response.data.user); // Save user data
                }
            } catch (error) {
                commit('setAuthenticated', false); // Login failed, set to false
                console.error('Login failed:', error);
            }
        },

        async logout({ commit }) {
            try {
                await axios.post('/api/auth/logout');
                commit('setAuthenticated', false); // Set to false after logout
                commit('clearUser'); // Clear user data
            } catch (error) {
                console.error('Logout failed:', error);
            }
        },

        async fetchUser({ commit }) {
            try {
                const response = await axios.get('/api/auth/users');
                commit('setUser', response.data); // Store user data
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        },
    },

    getters: {
        isAuthenticated(state) {
            return state.isAuthenticated; // Return the current authentication status
        },

        user(state) {
            return state.user; // Return the current user information
        },
    },
});
