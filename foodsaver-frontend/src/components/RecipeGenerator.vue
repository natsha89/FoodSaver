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

    <!-- Välj Allergier -->
    <v-select
        v-model="selectedAllergies"
        :items="allergyOptions"
        item-text="name"
        item-value="name"
        label="Select Allergies"
        multiple
        hint="Choose allergies to avoid"
        persistent-hint
    ></v-select>

    <!-- Välj Diet -->
    <v-select
        v-model="selectedDiets"
        :items="dietOptions"
        item-text="name"
        item-value="name"
        label="Select Diet"
        multiple
        hint="Choose a diet to follow"
        persistent-hint
    ></v-select>

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
    <v-alert v-else-if="!loading" type="info" class="mt-3">
      No recipes generated. Please select food items to see suggestions!
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
      allergyOptions: [],
      dietOptions: [],
      loading: false
    };
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'user']),
    isGenerateButtonDisabled() {
      return this.selectedIngredients.length === 0 || this.loading;
    }
  },
  methods: {
    async fetchFoodItems() {
      try {
        const response = await axios.get('/api/foodItems');
        console.log('Food Items:', response.data.data);
        this.foodItems = response.data.data;
      } catch (error) {
        console.error("Error fetching food items:", error);
      }
    },
    async fetchAllergyOptions() {
      try {
        const response = await axios.get('/api/foodItems/allergies');
        console.log('Allergy Options:', response.data.data);
        this.allergyOptions = response.data.data;
      } catch (error) {
        console.error("Error fetching allergy options:", error);
      }
    },
    async fetchDietOptions() {
      try {
        const response = await axios.get('/api/diets');  // Assuming you have a diet endpoint
        console.log('Diet Options:', response.data.data);
        this.dietOptions = response.data.data;
      } catch (error) {
        console.error("Error fetching diet options:", error);
      }
    },
    async generateRecipes() {
      this.loading = true;
      try {
        const response = await axios.get('/api/recipes/generate', {
          params: {
            ingredients: this.selectedIngredients,
            allergies: this.selectedAllergies,
            dietaryPreferences: this.selectedDiets
          }
        });
        this.recipes = response.data;
      } catch (error) {
        console.error("Error generating recipes:", error);
      } finally {
        this.loading = false;
      }
    },
    async saveRecipe(recipe) {
      try {
        await axios.post('/api/recipes/save', recipe);
        alert('Recipe saved!');
      } catch (error) {
        console.error("Error saving recipe:", error);
      }
    },
    async removeRecipe(recipeId) {
      try {
        await axios.delete(`/api/recipes/${recipeId}`);
        this.recipes = this.recipes.filter(r => r.id !== recipeId);
      } catch (error) {
        console.error("Error deleting recipe:", error);
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
</style>
