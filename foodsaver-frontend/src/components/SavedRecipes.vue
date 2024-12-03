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
                <!-- Buttons placed next to each other -->
                <v-btn color="primary" @click="openRecipeDialog(recipe)">
                  View Recipe
                </v-btn>
                <v-btn color="error" @click="deleteRecipe(recipe.id)">
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
          <p v-if="selectedRecipe?.instructions">
            {{ selectedRecipe.instructions }}
          </p>
          <p v-else>No instructions available for this recipe.</p>
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
      selectedRecipe: null // For storing the recipe to display in the dialog
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
    openRecipeDialog(recipe) {
      this.selectedRecipe = recipe;
      this.dialog = true; // Open the dialog
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
