<template>
  <v-container class="recipe-generator">
    <h2 class="page-title">Recipe Generator</h2>

    <v-form @submit.prevent="addIngredient" class="ingredient-form">
      <!-- Ingredient Input Field -->
      <v-text-field
          v-model="newIngredient"
          label="Ingredient"
          append-icon="mdi-plus"
          @click:append="addIngredient"
          @keyup.enter.prevent="addIngredient"
          class="ingredient-input"
          full-width
      ></v-text-field>

      <v-chip-group v-if="selectedIngredients.length" class="ingredient-chip-group">
        <v-chip
            v-for="(ingredient, index) in selectedIngredients"
            :key="index"
            close
            @click:close="selectedIngredients.splice(index, 1)"
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
          full-width
          class="diet-select"
      ></v-select>

      <!-- Servings Input Field -->
      <v-text-field
          v-model.number="selectedServing"
          type="number"
          label="Servings"
          min="1"
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
    <v-dialog v-model="dialogVisible" max-width="600">
      <v-card>
        <v-card-title class="headline">Generated Recipes</v-card-title>
        <v-card-text>
          <v-sheet
              v-if="generatedRecipes.length"
              class="pa-4 overflow-y-auto"
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
/* Styling for the Recipe Generator Page */
.recipe-generator {
  text-align: center;
  margin-top: 40px;
}

.page-title {
  font-size: 2.5em;
  font-weight: bold;
  color: #2E7D32;
  margin-bottom: 20px;
}

.ingredient-form {
  max-width: 600px;
  margin: 0 auto;
}

.ingredient-input,
.allergy-select,
.diet-select,
.serving-input {
  margin-bottom: 20px;
  max-width: 100%;
}

.generate-button {
  border-radius: 25px;
  font-weight: bold;
}

.ingredient-chip-group {
  margin: 20px 0;
}

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

/* Rotate animation for fun effect */
@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.recipe-details h3 {
  margin-top: 10px;
  margin-bottom: 5px;
}

.recipe-details p {
  margin-bottom: 10px;
}
</style>
