<template>
  <div>
    <h2>My Saved Recipes</h2>
    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>

    <template v-else>
      <v-container v-if="recipes.length">
        <v-row>
          <v-col
              v-for="recipe in recipes"
              :key="recipe.id"
              cols="12"
              sm="6"
              md="4"
              lg="3"
          >
            <v-card outlined>
              <v-card-title>{{ recipe.name }}</v-card-title>
              <v-card-actions>
                <v-btn color="primary" @click="openRecipeDialog(recipe)">
                  View
                </v-btn>
                <v-btn color="error" @click="confirmDelete(recipe.id)">
                  Delete
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      <p v-else>No recipes found.</p>
    </template>

    <!-- Dialog for showing recipe details -->
    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>{{ selectedRecipe?.name }}</v-card-title>
        <v-card-text>
          <div v-if="selectedRecipe">
            <h3>Ingredients:</h3>
            <ul v-if="selectedRecipe.ingredients?.length">
              <li v-for="(ingredient, index) in selectedRecipe.ingredients" :key="index">
                {{ ingredient }}
              </li>
            </ul>
            <p v-else>No ingredients listed.</p>

            <h3>Instructions:</h3>
            <p v-if="selectedRecipe.instructions">
              {{ selectedRecipe.instructions }}
            </p>
            <p v-else>No instructions available for this recipe.</p>

            <!-- Serving Size Section -->
            <h3>Serving:</h3>
            <p v-if="selectedRecipe.serving">
              {{ selectedRecipe.serving }} Serving
            </p>
            <p v-else>No serving information available.</p>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" text @click="dialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: 'SavedRecipes',
  data() {
    return {
      loading: true,
      dialog: false,
      selectedRecipe: null
    };
  },
  computed: {
    recipes() {
      return this.$store.getters.recipes;
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
        this.$store.dispatch('showSnackbar', {
          message: 'Failed to load recipes. Please try again.',
          color: 'error'
        });
      } finally {
        this.loading = false;
      }
    },
    openRecipeDialog(recipe) {
      this.selectedRecipe = recipe;
      this.dialog = true;
    },
    confirmDelete(recipeId) {
      if (confirm('Are you sure you want to delete this recipe?')) {
        this.deleteRecipe(recipeId);
      }
    },
    async deleteRecipe(recipeId) {
      try {
        await this.$store.dispatch('deleteRecipe', recipeId);
        this.$toast.success('Recipe deleted');
      } catch (error) {
        this.$toast.error('Failed to delete recipe');
      }
    }
  }
};
</script>
