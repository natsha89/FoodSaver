<template>
  <v-container class="recipe-generator">
    <h3>Recipe Suggestions</h3>

    <!-- Välj Matvaror -->
    <v-select
        v-model="selectedIngredients"
        :items="foodItems"
        item-text="name"
        item-value="id"
        label="Select Ingredients"
        multiple
        hint="Choose one or more food items"
        persistent-hint
    ></v-select>

    <!-- Allergier (autocomplete lista) -->
    <v-autocomplete
        v-model="selectedAllergies"
        :items="allergyOptions"
        label="Select Allergies"
        multiple
        chips
        clearable
        hint="Select any allergies you have"
    ></v-autocomplete>

    <!-- Dietpreferenser (autocomplete lista) -->
    <v-autocomplete
        v-model="selectedDiets"
        :items="dietOptions"
        label="Select Dietary Preferences"
        multiple
        chips
        clearable
        hint="Select any dietary preferences"
    ></v-autocomplete>

    <!-- Knapp för att generera recept -->
    <v-btn @click="generateRecipes" :disabled="isGenerateButtonDisabled">
      <span v-if="loading">Generating...</span>
      <span v-else>Generate Recipes</span>
    </v-btn>

    <!-- Laddningsindikator -->
    <v-progress-circular
        v-if="loading"
        indeterminate
        color="primary"
        size="30"
        class="my-2"
    ></v-progress-circular>

    <!-- Visar genererade recept -->
    <div v-if="recipes.length > 0">
      <h4>Generated Recipes:</h4>
      <v-list>
        <v-list-item-group>
          <v-list-item v-for="recipe in recipes" :key="recipe.id">
            <v-list-item-content>
              <v-list-item-title>{{ recipe.title }}</v-list-item-title>
              <v-list-item-subtitle>{{ recipe.instructions }}</v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn @click="saveRecipe(recipe)" color="primary">Save Recipe</v-btn>
              <v-btn @click="removeRecipe(recipe.id)" color="red">Delete Recipe</v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </div>

    <!-- Visar ett meddelande om inga recept genereras -->
    <v-alert v-else-if="!loading && recipes.length === 0" type="info" class="mt-3">
      No recipes generated. Please select food items to see suggestions!
    </v-alert>

    <!-- Loading state for fetching data -->
    <v-alert v-if="isFetchingData" type="info" class="mt-3">
      Loading food items, allergies, and diets...
    </v-alert>
  </v-container>
</template>

<script>
import axios from 'axios';
import { mapGetters } from 'vuex';

export default {
  data() {
    return {
      foodItems: [],
      selectedIngredients: [],
      selectedAllergies: [],
      selectedDiets: [],
      recipes: [],
      allergyOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs', 'None'], // Allergy options for autocomplete
      dietOptions: ['Vegan', 'Vegetarian', 'Keto', 'Paleo', 'None'],    // Diet options for autocomplete
      loading: false,
      isFetchingData: true,  // State for data fetching loading indicator
    };
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'user']),
    isGenerateButtonDisabled() {
      return this.selectedIngredients.length === 0 || this.loading;
    }
  },
  methods: {
    // Fetch food items
    async fetchFoodItems() {
      this.isFetchingData = true;
      try {
        const response = await axios.get('/api/foodItems');
        this.foodItems = response.data.data;
      } catch (error) {
        console.error("Error fetching food items:", error);
        this.$notify.error("Failed to load food items.");
      } finally {
        this.isFetchingData = false;
      }
    },
    // Fetch allergy options
    async fetchAllergyOptions() {
      this.isFetchingData = true;
      try {
        const response = await axios.get('/api/foodItems/allergies');
        this.allergyOptions = response.data.data;
      } catch (error) {
        console.error("Error fetching allergy options:", error);
        this.$notify.error("Failed to load allergy options.");
      } finally {
        this.isFetchingData = false;
      }
    },
    // Fetch diet options
    async fetchDietOptions() {
      this.isFetchingData = true;
      try {
        const response = await axios.get('/api/diets');
        this.dietOptions = response.data.data;
      } catch (error) {
        console.error("Error fetching diet options:", error);
        this.$notify.error("Failed to load diet options.");
      } finally {
        this.isFetchingData = false;
      }
    },
    // Generate recipes based on selected options
    async generateRecipes() {
      this.loading = true;
      try {
        const response = await axios.post('/api/recipes/generate', {
          ingredients: this.selectedIngredients.map(ingredient => ingredient.name),
          allergies: this.selectedAllergies,
          dietaryPreferences: this.selectedDiets.join(', '),
          servings: 4  // Example, replace with dynamic value
        });
        this.recipes = response.data;
        if (this.recipes.length === 0) {
          this.$notify.info("No recipes found for the selected ingredients, allergies, or diets.");
        }
      } catch (error) {
        console.error("Error generating recipes:", error);
        this.$notify.error("Failed to generate recipes.");
      } finally {
        this.loading = false;
      }
    },
    // Save a recipe to user's profile or favorites
    async saveRecipe(recipe) {
      try {
        await axios.post('/api/recipes', recipe);
        this.$notify.success("Recipe saved successfully!");
      } catch (error) {
        console.error("Error saving recipe:", error);
        this.$notify.error("Failed to save recipe.");
      }
    },
    // Remove a recipe from user's saved recipes
    async removeRecipe(recipeId) {
      try {
        await axios.delete(`/api/recipes/${recipeId}`);
        this.recipes = this.recipes.filter(r => r.id !== recipeId);
        this.$notify.success("Recipe deleted successfully.");
      } catch (error) {
        console.error("Error deleting recipe:", error);
        this.$notify.error("Failed to delete recipe.");
      }
    }
  },
  mounted() {
    this.fetchFoodItems();
    this.fetchAllergyOptions();
    this.fetchDietOptions();
  }
};
</script>

<style scoped>
.recipe-generator {
  max-width: 800px;
  margin: auto;
}

.my-2 {
  margin-top: 1rem;
}
</style>
