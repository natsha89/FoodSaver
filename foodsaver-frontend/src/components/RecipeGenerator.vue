<template>
  <v-container class="recipe-generator">
    <h3>Recipe Suggestions</h3>

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

    <v-btn @click="generateRecipes" :disabled="isGenerateButtonDisabled">
      <span v-if="loading">Generating...</span>
      <span v-else>Generate Recipes</span>
    </v-btn>

    <v-progress-circular
        v-if="loading"
        indeterminate
        color="primary"
        size="30"
        class="my-2"
    ></v-progress-circular>

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
      recipes: [],
      allergyOptions: [],
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
        if (this.isAuthenticated) {
          const userId = this.user.id; // Hämta det aktuella användar-ID:t
          const response = await axios.get(`/api/foodItems/user/${userId}`);
          this.foodItems = response.data;
        } else {
          const response = await axios.get('/api/foodItems/edamam');
          this.foodItems = response.data;
        }
      } catch (error) {
        console.error("Error fetching food items:", error);
      }
    },
    async fetchAllergyOptions() {
      try {
        const response = await axios.get('/api/foodItems/allergies');
        this.allergyOptions = response.data;
      } catch (error) {
        console.error("Error fetching allergy options:", error);
      }
    },
    async generateRecipes() {
      this.loading = true;
      try {
        const ingredients = this.selectedIngredients.map(item => item.id); // Hämta ID:n på valda ingredienser
        const allergies = this.selectedAllergies;

        const response = await axios.post('/api/recipes/generate', {
          ingredients: ingredients,
          allergies: allergies
        });

        this.recipes = response.data; // Spara genererade recept
      } catch (error) {
        console.error("Error generating recipes:", error);
      } finally {
        this.loading = false; // Återställ laddningstillstånd
      }
    },
    async saveRecipe(recipe) {
      // Kontrollera om användaren är inloggad
      if (!this.isAuthenticated) {
        alert("Du måste logga in för att spara recept. Vänligen logga in för att fortsätta.");
        this.$router.push({ name: 'Login' });
        return;
      }

      try {
        const savedRecipe = {
          ...recipe,          // Kopiera receptinformation
          userId: this.user.id // Lägg till userId
        };
        await axios.post('/api/recipes', savedRecipe);
        alert("Recept sparat framgångsrikt!");
      } catch (error) {
        console.error("Error saving recipe:", error);
      }
    },
    async removeRecipe(id) {
      if (!this.isAuthenticated) {
        alert("Du måste logga in för att ta bort recept. Vänligen logga in för att fortsätta.");
        this.$router.push({ name: 'Login' });
        return;
      }

      try {
        await axios.delete(`/api/recipes/${id}`); // Anropa DELETE endpointen för att ta bort receptet
        this.recipes = this.recipes.filter(recipe => recipe.id !== id); // Ta bort receptet från listan
        alert("Receptet har tagits bort framgångsrikt!");
      } catch (error) {
        console.error("Error deleting recipe:", error);
      }
    }
  },
  mounted() {
    this.fetchFoodItems(); // Hämta matvaror vid montering
    this.fetchAllergyOptions(); // Hämta allergier
  }
};
</script>

<style scoped>
.recipe-generator {
  max-width: 600px;
  margin: auto;
}
</style>
