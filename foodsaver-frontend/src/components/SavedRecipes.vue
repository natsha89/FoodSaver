<template>
  <v-container>
    <!-- If the user is not authenticated, show a warning -->
    <v-alert v-if="!isAuthenticated" type="warning">
      You must be logged in to view saved recipes.
      <v-btn @click="goToLogin" color="primary">Login</v-btn> <!-- Button to go to Login page -->
    </v-alert>

    <!-- If the user is authenticated, show the saved recipes -->
    <v-card v-else class="mb-4">
      <v-card-title>Saved Recipes</v-card-title>
      <v-card-text>
        <!-- Loading indicator while fetching saved recipes -->
        <v-progress-circular v-if="isLoading" indeterminate color="primary" size="30" class="my-2"></v-progress-circular>

        <!-- If no saved recipes, show an alert -->
        <v-alert v-else-if="savedRecipes.length === 0" type="info" class="mt-3">
          You don't have any saved recipes yet.
        </v-alert>

        <!-- List of saved recipes -->
        <v-list v-else>
          <v-list-item v-for="(recipe, index) in savedRecipes" :key="recipe.id">
            <v-list-item-content>{{ recipe.title }}</v-list-item-content>
            <v-list-item-action>
              <v-btn icon @click="removeRecipe(recipe.id, index)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'; // Import Vuex mapGetters
import axios from 'axios';

export default {
  data() {
    return {
      savedRecipes: [], // Initially empty, will store saved recipes
      isLoading: false, // Loading state for fetching saved recipes
    };
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'user']), // Map isAuthenticated and user from Vuex
  },
  methods: {
    goToLogin() {
      this.$router.push({ name: 'Login' }); // Navigate to the login page
    },
    async fetchSavedRecipes() {
      if (!this.isAuthenticated) return; // Don't fetch if not authenticated
      this.isLoading = true; // Show loading indicator
      try {
        const userId = this.user.id; // Get the user's ID from Vuex
        const response = await axios.get(`/api/recipes/${userId}`); // Fetch saved recipes from the backend
        this.savedRecipes = response.data; // Set saved recipes data
      } catch (error) {
        console.error('Error fetching saved recipes:', error);
        this.$notify.error('Failed to load saved recipes.'); // Optionally notify the user
      } finally {
        this.isLoading = false; // Hide loading indicator
      }
    },
    async removeRecipe(recipeId, index) {
      try {
        await axios.delete(`/api/recipes/${recipeId}`); // Delete recipe from the backend
        this.savedRecipes.splice(index, 1); // Remove recipe from list
        this.$notify.success('Recipe removed successfully!'); // Optionally notify the user
      } catch (error) {
        console.error('Error removing recipe:', error);
        this.$notify.error('Failed to remove recipe.'); // Optionally notify the user
      }
    },
  },
  watch: {
    // Watch for changes in authentication state
    isAuthenticated(newVal) {
      if (newVal) {
        this.fetchSavedRecipes(); // Fetch saved recipes when authenticated
      } else {
        this.savedRecipes = []; // Clear saved recipes when not authenticated
      }
    },
  },
  mounted() {
    if (this.isAuthenticated) {
      this.fetchSavedRecipes(); // Fetch saved recipes when the component is mounted
    }
  },
};
</script>

<style scoped>
/* Optional styling */
</style>
