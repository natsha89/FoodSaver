<template>
  <v-container class="recipe-generator">
    <h3>Recipe Suggestions</h3>

    <!-- Ingredient Selection -->
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

    <!-- Allergy Selection -->
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

    <v-btn @click="generateRecipes" :disabled="selectedIngredients.length === 0 || loading">
      <span v-if="loading">Generating...</span>
      <span v-else>Generate Recipes</span>
    </v-btn>

    <!-- Loading Indicator -->
    <v-progress-circular
        v-if="loading"
        indeterminate
        color="primary"
        size="30"
        class="my-2"
    ></v-progress-circular>

    <!-- Displaying Generated Recipes -->
    <div v-if="recipes.length > 0">
      <h4>Generated Recipes:</h4>
      <v-list>
        <v-list-item-group>
          <v-list-item v-for="recipe in recipes" :key="recipe.id">
            <v-list-item-content>
              <v-list-item-title>{{ recipe.title }}</v-list-item-title>
              <v-list-item-subtitle>{{ recipe.instructions }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </div>

    <v-alert v-else-if="recipes.length === 0 && !loading" type="info" class="mt-3">
      No recipes generated. Please select food items to see suggestions!
    </v-alert>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      foodItems: [], // List of foodItems fetched from the API
      selectedIngredients: [], // Ingredients selected by the user
      selectedAllergies: [], // Allergies selected by the user
      recipes: [], // List of generated recipes
      allergyOptions: [
        { name: 'Dairy-free' },
        { name: 'Gluten-free' },
        { name: 'Nut-free' }
      ], // Example allergy options formatted as objects
      loading: false // Loading state for the button and spinner
    };
  },
  methods: {
    async generateRecipes() {
      this.loading = true; // Set loading state to true
      try {
        const response = await axios.get('/api/recipes/search', {
          params: {
            query: this.selectedIngredients.join(','), // Send selected ingredients
            allergies: this.selectedAllergies // Send selected allergies
          }
        });
        this.recipes = response.data; // Store generated recipes in the component's state
      } catch (error) {
        console.error("Error generating recipes:", error);
      } finally {
        this.loading = false; // Reset loading state
      }
    }
  },
  mounted() {
    // Fetch foodItems from the API when the component is mounted
    axios.get('/api/foodItems')
        .then(response => {
          this.foodItems = response.data; // Save foodItems in the component's state
        })
        .catch(error => {
          console.error("Error fetching foodItems:", error);
        });
  }
};
</script>

<style scoped>
.recipe-generator {
  margin-top: 20px; /* Add margin to the top */
}
h3 {
  color: #4CAF50; /* Green color for the heading */
  margin-bottom: 15px; /* Add margin below the heading */
}
.v-btn:disabled {
  background-color: #BDBDBD; /* Gray color for disabled button */
  color: white; /* White text for disabled button */
}
</style>
