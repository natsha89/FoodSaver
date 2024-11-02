<template>
  <v-container>
    <h3>Add a New Food Item</h3>
    <v-form @submit.prevent="submitFoodItem">
      <v-text-field
          v-model="newFoodItem.name"
          label="Food Item Name"
          required
      ></v-text-field>
      <v-text-field
          v-model="newFoodItem.quantity"
          label="Quantity"
          type="number"
          required
      ></v-text-field>
      <v-select
          v-model="newFoodItem.unit"
          :items="units"
          label="Unit"
          required
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
      <v-btn type="submit" color="success">Add Food Item</v-btn>
    </v-form>

    <v-list>
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
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
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
      units: ['kg', 'g', 'lbs', 'oz', 'liters', 'ml'], // Add other units as needed
      allergenOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs'] // Example allergen options
    };
  },
  mounted() {
    this.fetchFoodItems();
    this.fetchUserAllergies();
  },
  methods: {
    fetchFoodItems() {
      // Fetch food items from the API
      axios.get('/api/foodItems/yourUserId') // Replace with the actual userId
          .then(response => {
            this.foodItems = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the food items:", error);
          });
    },
    fetchUserAllergies() {
      axios.get('/api/user/allergies') // Modify this endpoint according to your API
          .then(response => {
            this.userAllergies = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the user's allergies:", error);
          });
    },
    submitFoodItem() {
      axios.post('/api/foodItems', this.newFoodItem)
          .then(response => {
            this.foodItems.push(response.data); // Add the newly created food item to the list
            this.resetNewFoodItem(); // Reset form fields
          })
          .catch(error => {
            console.error("There was an error adding the food item:", error);
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
/* Add any necessary styles */
</style>
