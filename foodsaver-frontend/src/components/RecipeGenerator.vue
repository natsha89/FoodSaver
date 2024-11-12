<template>
  <v-container class="recipe-generator">
    <h3>Recipe Suggestions</h3>

    <!-- Textfält för att lägga till ingredienser -->
    <v-text-field
        v-model="newIngredient"
        label="Enter an ingredient and press Enter"
        @keyup.enter="addIngredient"
        clearable
        persistent-hint
        hint="Press Enter to add each ingredient"
    ></v-text-field>

    <!-- Visar tillagda ingredienser som chips -->
    <v-chip-group
        v-if="selectedIngredients.length > 0"
        multiple
        active-class="primary--text"
    >
      <v-chip
          v-for="(ingredient, index) in selectedIngredients"
          :key="index"
          close
          @click:close="removeIngredient(index)"
      >
        {{ ingredient }}
      </v-chip>
    </v-chip-group>

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

    <!-- Portioner (antal portioner input) -->
    <v-text-field
        v-model="selectedServing"
        label="Number of servings"
        type="number"
        min="1"
        hint="Specify the number of servings"
        persistent-hint
    ></v-text-field>

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

    <!-- Visar genererade recept med expanderbar panel -->
    <div v-if="recipes.length > 0">
      <h4>Generated Recipes:</h4>
      <v-expansion-panels>
        <v-expansion-panel v-for="recipe in recipes" :key="recipe.id">
          <v-expansion-panel-header>{{ recipe.name }}</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-card outlined class="my-3">
              <v-card-title>Ingredients</v-card-title>
              <v-card-text>
                <ul>
                  <li v-for="ingredient in recipe.ingredients" :key="ingredient">{{ ingredient }}</li>
                </ul>
              </v-card-text>
              <v-card-title>Instructions</v-card-title>
              <v-card-text>{{ recipe.instructions }}</v-card-text>
              <v-card-actions>
                <v-btn @click="saveRecipe(recipe)" color="primary">Save Recipe</v-btn>
                <v-btn @click="removeRecipe(recipe.id)" color="red">Delete Recipe</v-btn>
              </v-card-actions>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </div>

    <!-- Meddelande om inga recept genereras -->
    <v-alert v-else-if="!loading && recipes.length === 0" type="info" class="mt-3">
      No recipes generated. Please enter ingredients to see suggestions!
    </v-alert>
  </v-container>
</template>

<script>
import http from '../http'; // Konfiguration för HTTP-förfrågningar

export default {
  name: 'RecipeGenerator',
  data() {
    return {
      newIngredient: '', // För textfält för ingredienser
      selectedIngredients: [], // Lista av valda ingredienser
      selectedAllergies: [],
      selectedDiets: [],
      selectedServing: 1, // Förvalt antal portioner
      recipes: [],
      allergyOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs', 'None'],
      dietOptions: ['Vegan', 'Vegetarian', 'Keto', 'Paleo', 'None'],
      loading: false,
    };
  },
  computed: {
    isGenerateButtonDisabled() {
      return this.selectedIngredients.length === 0 || this.loading;
    },
  },
  methods: {
    addIngredient() {
      if (this.newIngredient.trim() !== '') {
        this.selectedIngredients.push(this.newIngredient.trim());
        this.newIngredient = '';
      }
    },
    removeIngredient(index) {
      this.selectedIngredients.splice(index, 1);
    },
    async generateRecipes() {
      if (this.selectedIngredients.length === 0) {
        this.$notify.info("Please add at least one ingredient.");
        return;
      }

      this.loading = true;
      try {
        const response = await http.post('/api/recipes/generate', {
          ingredients: this.selectedIngredients.join(', '),
          allergens: this.selectedAllergies,
          dietaryPreferences: this.selectedDiets.join(', '),
          servings: this.selectedServing,
        });

        // Kontrollera om svar är en lista av recept
        if (response.data && Array.isArray(response.data)) {
          this.recipes = response.data.length > 0 ? response.data : [];
          if (this.recipes.length > 0) {
            this.$notify.success("Recipes generated successfully!");
          } else {
            this.$notify.info("No recipes found with the selected ingredients.");
          }
        } else {
          throw new Error("Unexpected response format from server.");
        }
      } catch (error) {
        console.error("Error generating recipes:", error);
        this.$notify.error("Failed to generate recipes.");
      } finally {
        this.loading = false;
      }
    },
    async saveRecipe(recipe) {
      const userId = this.currentUser.id;  // Anta att användarens ID är tillgängligt via `currentUser`
      recipe.userId = userId;

      try {
        await http.post('/api/recipes', recipe, { params: { userId } });
        this.$notify.success("Recipe saved successfully!");
      } catch (error) {
        console.error("Error saving recipe:", error);
        this.$notify.error("Failed to save recipe.");
      }
  },
    async fetchUserRecipes() {
      const userId = this.currentUser.id;

      try {
        const response = await http.get(`/api/recipes`, { params: { userId } });
        this.recipes = response.data;
      } catch (error) {
        console.error("Error fetching user recipes:", error);
        this.$notify.error("Failed to fetch recipes.");
      }
    },

    async removeRecipe(recipeId) {
      try {
        await http.delete(`/api/recipes/${recipeId}`);
        this.recipes = this.recipes.filter(r => r.id !== recipeId);
        this.$notify.success("Recipe deleted successfully.");
      } catch (error) {
        console.error("Error deleting recipe:", error);
        this.$notify.error("Failed to delete recipe.");
      }
    }
  },
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
.my-3 {
  margin-top: 1.5rem;
}
</style>

