<template>
  <v-container>
    <h3>Add a New Food Item</h3>

    <!-- If the user is not authenticated, show login alert -->
    <v-alert v-if="!isAuthenticated" type="warning">
      You must be logged in to view this page.
      <v-btn @click="goToLogin" color="primary">Login</v-btn>
    </v-alert>

    <!-- The form to add a new food item (only visible when authenticated) -->
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

    <!-- Show loading spinner when data is being fetched -->
    <v-progress-circular v-if="loading" indeterminate color="primary"></v-progress-circular>
  </v-container>
</template>

<script>
import {mapGetters} from 'vuex';
import http from '../http'; // Import the centralized API handler

export default {
  data() {
    return {
      valid: false,
      newFoodItem: {
        name: '',
        quantity: '',
        unit: '',
        expirationDate: '',
        allergens: []
      },
      menu: false,
      loading: false,
      errorMessage: '',
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
    }
  },
  methods: {
    goToLogin() {
      this.$router.push({name: 'Login'});
    },
    // Fetch the list of food items
    fetchFoodItems() {
      this.loading = true;
      http.get('/foodItems')
          .then(response => {
            this.foodItems = response.data;
          })
          .catch(() => {
            this.errorMessage = "Error fetching food items. Please try again later.";
          })
          .finally(() => {
            this.loading = false;
          });
    },
    // Submit a new food item
    submitFoodItem() {
      this.loading = true;
      http.post('/foodItems', this.newFoodItem)
          .then(response => {
            this.foodItems.push(response.data);
            this.resetNewFoodItem();
          })
          .catch(() => {
            this.errorMessage = "Error adding food item. Please try again later.";
          })
          .finally(() => {
            this.loading = false;
          });
    },
    // Reset the new food item form
    resetNewFoodItem() {
      this.newFoodItem = {name: '', quantity: '', unit: '', expirationDate: '', allergens: []};
    },

    // New method for redirection on button click
    redirectToFoodItems() {
      if (!this.isAuthenticated) {
        this.$router.push({name: 'Login'}); // Redirect to login if not authenticated
      } else {
        this.$router.push({name: 'FoodItems'}); // Redirect to food items page if authenticated
      }
    }
  }
};
</script>
