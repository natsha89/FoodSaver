<template>
  <div>
    <h2>My Saved Recipes</h2>
    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>
    <v-list v-else-if="recipes.length">
      <v-list-item v-for="recipe in recipes" :key="recipe.id" class="mb-2">
        <v-list-item-content>
          <v-list-item-title>{{ recipe.name }}</v-list-item-title>
          <v-list-item-subtitle>
            {{ recipe.description || 'No description available' }}
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn icon @click="viewRecipeDetails(recipe.id)">
            <v-icon>mdi-eye</v-icon>
          </v-btn>
          <v-btn icon color="error" @click="deleteRecipe(recipe.id)">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <p v-else>No recipes found.</p>
  </div>
</template>

<script>
export default {
  name: 'SavedRecipes',
  data() {
    return {
      loading: true
    };
  },
  computed: {
    recipes() {
      return this.$store.getters.recipes; // Update to use recipes getter
    }
  },
  created() {
    this.loadRecipes();
  },
  methods: {
    async loadRecipes() {
      try {
        this.loading = true;
        await this.$store.dispatch('fetchRecipes');
      } catch (error) {
        console.error('Failed to load recipes:', error);
        // Show error message to user
        this.$store.dispatch('showSnackbar', {
          message: 'Failed to load recipes. Please try again.',
          color: 'error'
        });
      } finally {
        this.loading = false;
      }
    },
    viewRecipeDetails(recipeId) {
      // Navigate to recipe details page
      this.$router.push(`/recipes/${recipeId}`);
    },
    async deleteRecipe(recipeId) {
      try {
        // Confirm deletion
        const confirm = await this.$dialog.confirm({
          text: 'Are you sure you want to delete this recipe?',
          title: 'Confirm Deletion'
        });

        if (confirm) {
          await this.$store.dispatch('deleteRecipe', recipeId);
          // Optional: show success message
          this.$store.dispatch('showSnackbar', {
            message: 'Recipe deleted successfully',
            color: 'success'
          });
        }
      } catch (error) {
        console.error('Failed to delete recipe:', error);
        // Show error message
        this.$store.dispatch('showSnackbar', {
          message: 'Failed to delete recipe. Please try again.',
          color: 'error'
        });
      }
    }
  }
};
</script>