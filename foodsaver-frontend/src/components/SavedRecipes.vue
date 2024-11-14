<template>
  <v-container>
    <!-- Saved Recipes card -->
    <v-card class="mb-4">
      <v-card-title>Saved Recipes</v-card-title>
      <v-card-text>
        <!-- Loading indicator -->
        <v-progress-circular v-if="isLoading" indeterminate color="primary" size="30" class="my-2"></v-progress-circular>

        <!-- No saved recipes message -->
        <v-alert v-else-if="savedRecipes.length === 0" type="info" class="mt-3">
          You don't have any saved recipes yet.
        </v-alert>

        <!-- Grid of recipe tiles -->
        <v-row v-else class="saved-recipes-grid">
          <v-col
              v-for="recipe in savedRecipes"
              :key="recipe.id"
              cols="12"
              sm="6"
              md="4"
          >
            <v-card
                class="recipe-tile"
                @click="openRecipeDialog(recipe)"
            >
              <v-card-title>{{ recipe.title }}</v-card-title>
              <v-card-subtitle class="truncate">{{ recipe.description }}</v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- Recipe dialog -->
    <v-dialog v-model="isDialogOpen" max-width="600px">
      <v-card>
        <v-card-title>{{ selectedRecipe.title }}</v-card-title>
        <v-card-text>{{ selectedRecipe.description }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="isDialogOpen = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import http from '../http';

export default {
  data() {
    return {
      savedRecipes: [],
      isLoading: false,
      isDialogOpen: false,
      selectedRecipe: {}
    };
  },
  methods: {
    async fetchSavedRecipes() {
      this.isLoading = true;
      try {
        const response = await http.get('/api/recipes');
        this.savedRecipes = response.data;
        console.log('Fetched recipes:', this.savedRecipes);
      } catch (error) {
        console.error('Error fetching saved recipes:', error);
        this.$notify.error('Failed to load saved recipes.');
      } finally {
        this.isLoading = false;
      }
    },
    openRecipeDialog(recipe) {
      this.selectedRecipe = recipe;
      this.isDialogOpen = true;
    },
    async removeRecipe(recipeId, index) {
      try {
        await http.delete(`/api/recipes/${recipeId}`);
        this.savedRecipes.splice(index, 1);
        this.$notify.success('Recipe removed successfully!');
      } catch (error) {
        console.error('Error removing recipe:', error);
        this.$notify.error('Failed to remove recipe.');
      }
    },
  },
  mounted() {
    this.fetchSavedRecipes();
  },
};
</script>

<style scoped>
.saved-recipes-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.recipe-tile {
  background-color: #e0f7fa; /* Teal background color */
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: transform 0.2s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.recipe-tile:hover {
  transform: scale(1.02);
}

.v-card-title {
  color: #004d40; /* Dark teal for title */
}

.truncate {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #00796b; /* Darker teal for subtitle text */
}
</style>