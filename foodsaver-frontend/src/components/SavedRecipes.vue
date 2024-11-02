<template>
  <v-container>
    <v-card class="mb-4">
      <v-card-title>Saved Recipes</v-card-title>
      <v-card-text>
        <v-list>
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
import axios from 'axios';

export default {
  data() {
    return {
      savedRecipes: [], // Initially empty, will store saved recipes
    };
  },
  methods: {
    async fetchSavedRecipes() {
      // Fetch saved recipes from the API when the component mounts
      try {
        const response = await axios.get(`/api/recipes/${this.userId}`); // Use actual user ID from context
        this.savedRecipes = response.data;
      } catch (error) {
        console.error('Error fetching saved recipes:', error);
      }
    },
    async removeRecipe(recipeId, index) {
      try {
        await axios.delete(`/api/recipes/${recipeId}`); // Delete recipe from the backend
        this.savedRecipes.splice(index, 1); // Remove the recipe by index
        console.log('Recipe removed!'); // Optional console log
      } catch (error) {
        console.error('Error removing recipe:', error);
      }
    },
  },
  mounted() {
    this.fetchSavedRecipes(); // Fetch saved recipes when the component is mounted
  },
};
</script>

<style scoped>
</style>
