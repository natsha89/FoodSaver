<template>
  <v-container>
    <h3>Add a New Food Item</h3>
    <v-alert v-if="!isAuthenticated" type="warning">
      You must be logged in to add food items.
      <v-btn @click="goToLogin" color="primary">Login</v-btn>
    </v-alert>

    <v-form v-else @submit.prevent="submitFoodItem" v-model="valid">
      <v-text-field
          v-model="newFoodItem.name"
          label="Food Item Name"
          required
          :rules="[v => !!v || 'Name is required']"
      ></v-text-field>
      <v-text-field
          v-model="newFoodItem.quantity"
          label="Quantity"
          type="number"
          required
          :rules="[v => !!v || 'Quantity is required']"
      ></v-text-field>
      <v-select
          v-model="newFoodItem.unit"
          :items="units"
          label="Unit"
          required
          :rules="[v => !!v || 'Unit is required']"
      ></v-select>
      <v-menu
          v-model="menu"
          ref="menu"
          :close-on-content-click="false"
          transition="scale-transition"
          offset-y
          min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
              v-model="newFoodItem.expirationDate"
              label="Expiration Date"
              append-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              required
          ></v-text-field>
        </template>
        <v-date-picker
            v-model="newFoodItem.expirationDate"
            @input="menu = false"
        ></v-date-picker>
      </v-menu>
      <v-autocomplete
          v-model="newFoodItem.allergens"
          :items="allergenOptions"
          label="Allergens (optional)"
          multiple
          chips
          clearable
      ></v-autocomplete>
      <v-btn type="submit" color="success" :disabled="!valid || loading">Add Food Item</v-btn>
      <v-alert v-if="errorMessage" type="error" class="mt-3">{{ errorMessage }}</v-alert>
    </v-form>

    <v-list v-if="!loading">
      <v-list-item-group>
        <v-list-item v-for="foodItem in foodItems" :key="foodItem.id">
          <v-list-item-content>
            <v-list-item-title>{{ foodItem.name }}</v-list-item-title>
            <v-list-item-subtitle>
              {{ foodItem.quantity }} {{ foodItem.unit }} - Expires: {{ foodItem.expirationDate }}
            </v-list-item-subtitle>
            <v-alert v-if="foodItem.allergens.some(allergen => userAllergies.includes(allergen))" type="error" class="mt-2">
              Warning: This food item contains allergens you are sensitive to!
            </v-alert>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
    <v-progress-circular v-if="loading" indeterminate color="primary"></v-progress-circular>
  </v-container>
</template>

<script>
import {mapGetters} from 'vuex';
import axios from 'axios';

export default {
  data() {
    return {
      valid: false, // Validation flag
      foodItems: [],
      newFoodItem: {
        name: '',
        quantity: '',
        unit: '',
        expirationDate: '',
        allergens: []
      },
      userAllergies: [],
      menu: false,
      loading: false,
      errorMessage: '', // Error message state
      units: ['kg', 'g', 'lbs', 'oz', 'liters', 'ml'],
      allergenOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs']
    };
  },
  computed: {
    ...mapGetters(['isAuthenticated']),
  },
  mounted() {
    if (this.isAuthenticated) {
      this.fetchFoodItems();
      this.fetchUserAllergies();
    }
  },
  methods: {
    goToLogin() {
      this.$router.push({name: 'Login'});
    },
    fetchFoodItems() {
      this.loading = true; // Start loading
      axios.get('/api/foodItems/yourUserId')
          .then(response => {
            this.foodItems = response.data;
          })
          .catch(error => {
            this.errorMessage = "Error fetching food items. Please try again later.";
            console.error("Error fetching food items:", error);
          })
          .finally(() => {
            this.loading = false; // End loading
          });
    },
    fetchUserAllergies() {
      this.loading = true; // Start loading
      axios.get('/api/user/allergies')
          .then(response => {
            this.userAllergies = response.data;
          })
          .catch(error => {
            this.errorMessage = "Error fetching allergies. Please try again later.";
            console.error("Error fetching allergies:", error);
          })
          .finally(() => {
            this.loading = false; // End loading
          });
    },
    submitFoodItem() {
      this.loading = true; // Start loading
      axios.post('/api/foodItems', this.newFoodItem)
          .then(response => {
            this.foodItems.push(response.data);
            this.resetNewFoodItem();
          })
          .catch(error => {
            this.errorMessage = "Error adding food item. Please check your input and try again.";
            console.error("Error adding food item:", error);
          })
          .finally(() => {
            this.loading = false; // End loading
          });
    },
    resetNewFoodItem() {
      this.newFoodItem = {
        name: '',
        quantity: '',
        unit: '',
        expirationDate: '',
        allergens: []
      };
    }
  }
};
</script>

<style scoped>
/* Add necessary styles */
</style>
