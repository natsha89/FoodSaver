<template>
  <v-container class="recipe-generator">
    <h2 class="page-title">Recipe Generator</h2>
    <div class="image-container">
      <img src="/generatore.png" alt="Recipe Generator" class="recipe-image"/>
    </div>
    <v-form @submit.prevent="addIngredient" class="ingredient-form">
      <!-- Ingredient Input Field -->
      <v-text-field
          v-model="newIngredient"
          label="Add Ingredient"
          append-icon="mdi-plus"
          @click:append="addIngredient"
          @keyup.enter.prevent="addIngredient"
          outlined
          full-width
          class="ingredient-input"
      ></v-text-field>

      <v-chip-group v-if="selectedIngredients.length" class="ingredient-chip-group">
        <v-chip
            v-for="(ingredient, index) in selectedIngredients"
            :key="index"
            close
            @click:close="selectedIngredients.splice(index, 1)"
            class="ingredient-chip"
        >
          {{ ingredient }}
        </v-chip>
      </v-chip-group>

      <!-- Allergy Selection -->
      <v-select
          v-model="selectedAllergies"
          :items="allergyOptions"
          label="Allergies"
          multiple
          chips
          outlined
          full-width
          class="allergy-select"
      ></v-select>

      <!-- Diet Preference Selection -->
      <v-select
          v-model="selectedDiets"
          :items="dietOptions"
          label="Dietary Preferences"
          multiple
          chips
          outlined
          full-width
          class="diet-select"
      ></v-select>

      <!-- Servings Input Field -->
      <v-text-field
          v-model.number="selectedServing"
          type="number"
          label="Servings"
          min="1"
          outlined
          full-width
          class="serving-input"
      ></v-text-field>

      <v-btn
          color="primary"
          @click="generateRecipes"
          :disabled="!selectedIngredients.length || loading"
          class="generate-button"
      >
        Generate Recipes
      </v-btn>
    </v-form>

    <!-- Custom loading animation -->
    <div v-if="loading" class="loading-container">
      <img src="/chef.png" alt="Chef stirring ingredients" class="loading-image"/>
    </div>

    <!-- Recipe Dialog Box -->
    <v-dialog v-model="dialogVisible" max-width="700px">
      <v-card class="dialog-card">
        <v-card-title class="headline">Generated Recipes</v-card-title>
        <v-card-text>
          <v-sheet
              v-if="generatedRecipes.length"
              class="pa-4 overflow-y-auto recipe-list"
              max-height="400"
          >
            <v-expansion-panels>
              <v-expansion-panel
                  v-for="(recipe, index) in generatedRecipes"
                  :key="index"
              >
                <v-expansion-panel-header>
                  {{ recipe.title }}
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                  <div class="recipe-details">
                    <h3>Ingredients:</h3>
                    <p v-for="(ingredient, idx) in recipe.ingredients" :key="idx">
                      - {{ ingredient }}
                    </p>

                    <h3>Instructions:</h3>
                    <p v-for="(instruction, idx) in recipe.instructions.split('\n')" :key="idx">
                      {{ instruction }}
                    </p>
                    <p><strong>Servings:</strong> {{ recipe.servings }}</p>
                  </div>
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
          </v-sheet>
          <p v-else class="text-center">No recipes generated.</p>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="dialogVisible = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      newIngredient: '',
      selectedIngredients: [],
      selectedAllergies: [],
      selectedDiets: [],
      selectedServing: 1,
      allergyOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs', 'None'],
      dietOptions: ['Vegan', 'Vegetarian', 'Keto', 'Paleo', 'None'],
      dialogVisible: false,
      generatedRecipes: [], // Added to store generated recipes
    };
  },
  methods: {
    addIngredient() {
      if (this.newIngredient.trim()) {
        this.selectedIngredients.push(this.newIngredient.trim());
        this.newIngredient = '';
      }
    },
    async generateRecipes() {
      try {
        this.loading = true;
        const recipeData = {
          ingredients: this.selectedIngredients.join(', '),
          allergens: this.selectedAllergies,
          dietaryPreferences: this.selectedDiets.join(', '),
          servings: this.selectedServing,
        };

        // Assuming the store dispatch returns the generated recipes
        this.generatedRecipes = await this.$store.dispatch('generateRecipe', recipeData);

        // Show dialog after successful generation
        this.dialogVisible = true;

        // Reset inputs
        this.selectedIngredients = [];
        this.selectedAllergies = [];
        this.selectedDiets = [];
        this.selectedServing = 1;
      } catch (error) {
        this.$toast.error('Failed to generate recipes');
        this.generatedRecipes = []; // Clear any previous recipes
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
/* General Styling */
.recipe-generator {
  text-align: center;
  margin-top: 40px;
  font-family: 'Roboto', sans-serif;
  background-color: #F9F9F9; /* Light background color for contrast */
}

.page-title {
  font-size: 2.5em;
  font-weight: bold;
  color: #388E3C; /* More vibrant green */
  margin-bottom: 20px;
}

.ingredient-form {
  max-width: 650px;
  margin: 0 auto;
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.ingredient-input,
.allergy-select,
.diet-select,
.serving-input {
  margin-bottom: 20px;
  max-width: 100%;
  border-radius: 8px;
}

.generate-button {
  border-radius: 25px;
  font-weight: bold;
  width: 100%;
  height: 50px;
  margin-top: 20px;
  transition: background-color 0.3s ease;
}

.generate-button:hover {
  background-color: #2E7D32;
}

.ingredient-chip-group {
  margin: 20px 0;
}

.ingredient-chip {
  border-radius: 20px;
  background-color: #81C784;
}

/* Loading Animation */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

.loading-image {
  width: 80px;
  height: 80px;
  animation: rotate 1.5s infinite linear;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Recipe Dialog Styling */
.dialog-card {
  background-color: #FFF;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.2);
}

.recipe-list {
  max-height: 400px;
  overflow-y: auto;
}

.recipe-details h3 {
  margin-top: 10px;
  margin-bottom: 5px;
}

.recipe-details p {
  margin-bottom: 10px;
}

.recipe-image {
  width: 100%;
  max-width: 500px;
  height: auto;
  margin-bottom: 20px;
}
</style>
