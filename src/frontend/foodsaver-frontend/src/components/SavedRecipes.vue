<template>
  <div>
    <h2 class="page-title">Saved Recipes</h2>
    <!-- Banner Image under "Saved Recipes" (myrecipe.png) -->
    <v-img
        src="/myrecipe.png"
        alt="My Saved Recipes"
        class="banner-image"
        aspect-ratio="16/9"
        max-height="400px"
    contain
    ></v-img>

    <v-progress-circular v-if="loading" indeterminate class="loading-spinner"></v-progress-circular>

    <template v-else>
      <v-container v-if="recipes.length" class="recipe-container">
        <v-row>
          <v-col
              v-for="recipe in recipes"
              :key="recipe.id"
              cols="12"
              sm="6"
              md="4"
              lg="3"
              class="recipe-col"
          >
            <v-card class="recipe-card" elevation="2" outlined>
              <!-- Common Image for Each Recipe (recipe.png) -->
              <v-img
                  src="/recipe.png"
                  alt="Recipe Image"
                  height="200px"
                  class="recipe-image"
              ></v-img>

              <v-card-title class="recipe-title">{{ recipe.name }}</v-card-title>
              <v-card-actions>
                <v-btn @click="openRecipeDialog(recipe)" color="primary" rounded small>
                  View
                </v-btn>
                <v-btn @click="confirmDelete(recipe.id)" color="error" rounded small>
                  Delete
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      <p v-else class="no-recipes-text">No saved recipes found.</p>
    </template>

    <!-- Recipe Detail Dialog -->
    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title class="dialog-title">{{ selectedRecipe?.name }}</v-card-title>
        <v-card-text>
          <div v-if="selectedRecipe">
            <h3 class="dialog-subheading">Ingredients:</h3>
            <ul v-if="selectedRecipe.ingredients?.length">
              <li v-for="(ingredient, index) in selectedRecipe.ingredients" :key="index">
                {{ ingredient }}
              </li>
            </ul>
            <p v-else class="no-ingredients">No ingredients listed.</p>

            <h3 class="dialog-subheading">Instructions:</h3>
            <p v-if="selectedRecipe.instructions" class="dialog-content">
              {{ selectedRecipe.instructions }}
            </p>
            <p v-else class="no-instructions">No instructions available.</p>

            <h3 class="dialog-subheading">Serving:</h3>
            <p v-if="selectedRecipe.serving" class="dialog-content">
              {{ selectedRecipe.serving }} servings
            </p>
            <p v-else class="dialog-content">No serving information available.</p>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" text @click="dialog = false" class="close-btn">Close</v-btn>
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

<style scoped>
.page-title {
  text-align: center;
  font-size: 2rem;
  color: #388E3C;
  font-weight: 600;
  margin-bottom: 20px;
}

.banner-image {
  border-radius: 8px;
  margin-top: 20px;
}

.loading-spinner {
  display: block;
  margin: 100px auto;
}

.recipe-container {
  margin-top: 20px;
}

.recipe-col {
  margin-bottom: 20px;
}

.recipe-card {
  transition: transform 0.2s, box-shadow 0.3s ease-in-out;
}

.recipe-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.recipe-image {
  border-radius: 8px 8px 0 0;
}

.recipe-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #388E3C;
  text-align: center;
}

.card-actions {
  justify-content: center;
}

.view-btn, .delete-btn {
  font-weight: 600;
  padding: 6px 12px; /* Reduced padding */
  font-size: 0.875rem; /* Smaller font size */
}

.no-recipes-text {
  text-align: center;
  font-size: 1.2rem;
  color: #888;
}

.dialog-title {
  font-size: 1.6rem;
  font-weight: bold;
  color: #388E3C;
}

.dialog-subheading {
  font-size: 1.2rem;
  font-weight: 600;
  color: #388E3C;
  margin-top: 20px;
}

.dialog-content {
  font-size: 1rem;
  color: #333;
}

.no-ingredients, .no-instructions {
  font-style: italic;
  color: #888;
}

.close-btn {
  font-weight: 600;
  color: #388E3C;
}
</style>
