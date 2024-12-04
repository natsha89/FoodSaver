<template>
  <div>
    <h2>Recipe Generator</h2>

    <v-form @submit.prevent="addIngredient">
      <v-text-field
          v-model="newIngredient"
          label="Ingredient"
          append-icon="mdi-plus"
          @click:append="addIngredient"
          @keyup.enter.prevent="addIngredient"
      ></v-text-field>

      <v-chip-group v-if="selectedIngredients.length">
        <v-chip
            v-for="(ingredient, index) in selectedIngredients"
            :key="index"
            close
            @click:close="selectedIngredients.splice(index, 1)"
        >
          {{ ingredient }}
        </v-chip>
      </v-chip-group>

      <v-select
          v-model="selectedAllergies"
          :items="allergyOptions"
          label="Allergies"
          multiple
          chips
      ></v-select>

      <v-select
          v-model="selectedDiets"
          :items="dietOptions"
          label="Dietary Preferences"
          multiple
          chips
      ></v-select>

      <v-text-field
          v-model.number="selectedServing"
          type="number"
          label="Servings"
          min="1"
      ></v-text-field>

      <v-btn
          color="primary"
          @click="generateRecipes"
          :disabled="!selectedIngredients.length || loading"
      >
        Generate Recipes
      </v-btn>
    </v-form>

    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>

    <!-- Dialog Box -->
    <v-dialog v-model="dialogVisible" max-width="500">
      <v-card>
        <v-card-title class="headline">Recipes Generated</v-card-title>
        <v-card-text>
          Your recipes have been successfully generated. Check them in your recipe book or save them for later!
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="dialogVisible = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
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
      dialogVisible: false, // State for dialog visibility
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

        await this.$store.dispatch('generateRecipe', recipeData);

        // Show dialog after successful generation
        this.dialogVisible = true;

        // Reset inputs
        this.selectedIngredients = [];
        this.selectedAllergies = [];
        this.selectedDiets = [];
        this.selectedServing = 1;
      } catch (error) {
        this.$toast.error('Failed to generate recipes');
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

