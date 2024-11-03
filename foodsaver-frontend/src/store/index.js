
import { createStore } from 'vuex'; // Importera createStore från vuex
import axios from 'axios';

// Skapa en ny Vuex store
export default createStore({
    state: {
        ingredients: [], // Delad state för ingredienser
    },
    mutations: {
        setIngredients(state, ingredients) {
            state.ingredients = ingredients; // Uppdatera ingredienser i state
        },
        clearIngredients(state) {
            state.ingredients = []; // Rensa ingredienser från state
        },
    },
    actions: {
        async fetchIngredients({ commit }) {
            try {
                const response = await axios.get('/api/ingredients'); // Gör API-anropet
                commit('setIngredients', response.data); // Commit ingredienser till state
            } catch (error) {
                console.error('Error fetching ingredients:', error); // Logga eventuella fel
            }
        },
    },
    getters: {
        allIngredients(state) {
            return state.ingredients; // Returnera alla ingredienser
        },
    },
});
