<template>
  <v-container>
    <!-- Toolbar -->
    <v-toolbar color="primary" dark>
      <v-toolbar-title>Food Items</v-toolbar-title>
    </v-toolbar>

    <!-- Lista av matvaror -->
    <v-card class="mt-5">
      <v-card-title>Available Food Items</v-card-title>
      <v-list>
        <v-list-item-group>
          <v-list-item
              v-for="(item, index) in foodItems"
              :key="index"
          >
            <v-list-item-content>
              <v-list-item-title>{{ item.name }}</v-list-item-title>
              <v-list-item-subtitle>Quantity: {{ item.quantity }} {{ item.unit }}</v-list-item-subtitle>
              <v-list-item-subtitle>Expiration Date: {{ item.expirationDate }}</v-list-item-subtitle>
              <v-list-item-subtitle>Allergens: {{ item.allergens.join(', ') }}</v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn @click="deleteFoodItem(index)" color="red">
                Delete
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-card>

    <!-- Formulär för att lägga till ny matvara -->
    <v-card class="mt-5">
      <v-card-title>Add New Food Item</v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="formValid">
          <!-- Food Item Name -->
          <v-text-field
              v-model="newItem.name"
              :rules="nameRules"
              label="Food Item Name"
              required
          ></v-text-field>

          <!-- Quantity Section -->
          <v-row class="d-flex align-center">
            <v-col cols="3" class="d-flex align-center">
              <v-btn @click="changeQuantity(-1)" :disabled="newItem.quantity <= 1" small outlined>-</v-btn>
              <v-text-field
                  v-model="newItem.quantity"
                  :rules="quantityRules"
                  type="number"
                  label="Quantity"
                  min="1"
                  hide-details
                  class="mx-2"
                  style="width: 70px;"
              ></v-text-field>
              <v-btn @click="changeQuantity(1)" small outlined>+</v-btn>
            </v-col>
          </v-row>

          <!-- Unit selection dropdown -->
          <v-select
              v-model="newItem.unit"
              :items="unitOptions"
              label="Unit"
              required
          ></v-select>

          <!-- Expiration Date -->
          <v-text-field
              v-model="newItem.expirationDate"
              :rules="expirationDateRules"
              label="Expiration Date"
              type="date"
              required
          ></v-text-field>

          <!-- Allergens selection dropdown -->
          <v-select
              v-model="newItem.allergens"
              :items="allergenOptions"
              label="Allergens"
              multiple
              required
          ></v-select>
        </v-form>
        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="addFoodItem" :loading="loading" :disabled="loading">Add Item</v-btn>
      </v-card-actions>
    </v-card>

    <!-- Alert för allergen varning -->
    <v-alert v-if="allergenAlert" type="warning" dismissible>
      This food item contains allergens you are allergic to: {{ allergenAlert }}
    </v-alert>

    <!-- Alert för utgångsdatum -->
    <v-alert v-if="expirationAlert" type="warning" dismissible>
      The expiration date for this food item is approaching: {{ expirationAlert }}
    </v-alert>
  </v-container>
</template>

<script>
import http from '../http';

export default {
  name: 'FoodItems',
  data() {
    return {
      foodItems: [],
      newItem: {
        name: '',
        quantity: 1,
        unit: '',
        expirationDate: '',
        allergens: [] // List of selected allergens
      },
      unitOptions: ['kg', 'g', 'liters', 'ml', 'pieces'], // Example unit options
      allergenOptions: ['Gluten', 'Peanuts', 'Dairy', 'Soy', 'Shellfish', 'Eggs'], // Example allergens
      formValid: false,
      loading: false,
      responseMessage: '',
      allergenAlert: '', // Alert for allergens
      expirationAlert: '', // Alert for expiration
      userAllergens: [], // User-specific allergens
      nameRules: [
        v => !!v || 'Name is required',
        v => v.length <= 50 || 'Name must be less than 50 characters'
      ],
      quantityRules: [
        v => !!v || 'Quantity is required',
        v => v > 0 || 'Quantity must be a positive number'
      ],
      expirationDateRules: [
        v => !!v || 'Expiration Date is required',
        v => /\d{4}-\d{2}-\d{2}/.test(v) || 'Invalid date format'
      ]
    };
  },
  methods: {
    async fetchUserData() {
      try {
        const response = await http.get('/api/auth/users');
        console.log('User data response:', response.data); // Logga för att se API-svaret

        // Hämta användarens ID från localStorage eller någon annan källa
        const userId = localStorage.getItem('userId'); // Exempel: om användarens ID finns i localStorage

        // Hitta användaren i listan
        const user = response.data.find(user => user.id === userId);

        if (user) {
          // Om användaren hittades, sätt allergenerna
          if (Array.isArray(user.allergies)) {
            this.userAllergens = user.allergies; // Hämta allergener för rätt användare
          } else {
            this.userAllergens = []; // Om allergener är felaktigt formaterade
            this.responseMessage = 'Allergies are missing or in wrong format.';
          }
        } else {
          // Om ingen användare hittades
          this.responseMessage = 'User not found in the list.';
        }
      } catch (error) {
        this.responseMessage = error.response?.data.message || error.message || 'Failed to fetch user data.';
      }
  },
    async fetchFoodItems() {
      const userId = localStorage.getItem('userId'); // Hämta userId från localStorage
      if (!userId) {
        this.responseMessage = 'User ID is not available in localStorage.';
        return;
      }
      try {
        const response = await http.get(`/api/foodItems/user/${userId}`);
        this.foodItems = response.data;
      } catch (error) {
        console.error('Error fetching food items:', error.message);
        this.responseMessage = error.response?.data.message || 'Failed to fetch food items.';
      }
    },
    async addFoodItem() {
      this.responseMessage = '';
      if (this.$refs.form.validate()) {
        this.loading = true;

        // Check for allergen matches
        const matchedAllergens = this.newItem.allergens.filter(allergen =>
            this.userAllergens.includes(allergen)
        );
        if (matchedAllergens.length > 0) {
          this.allergenAlert = `This food item contains allergens you are allergic to: ${matchedAllergens.join(', ')}`;
        } else {
          this.allergenAlert = '';
        }

        // Check expiration date
        const expirationDate = new Date(this.newItem.expirationDate);
        const today = new Date();
        const daysUntilExpiration = Math.floor((expirationDate - today) / (1000 * 60 * 60 * 24));
        if (daysUntilExpiration <= 3) {
          this.expirationAlert = `The expiration date for this food item is approaching: ${this.newItem.expirationDate}`;
        } else {
          this.expirationAlert = '';
        }

        // Add new food item
        try {
          const response = await http.post('/api/foodItems', this.newItem);
          if (response.status === 201) {
            this.foodItems.push(response.data.foodItem);
            this.newItem = { name: '', quantity: 1, unit: '', expirationDate: '', allergens: [] };
            this.allergenAlert = '';
            this.expirationAlert = '';
          }
        } catch (error) {
          this.responseMessage = error.response?.data.message || 'Failed to add food item.';
        } finally {
          this.loading = false;
        }
      }
    },
    changeQuantity(amount) {
      if (this.newItem.quantity + amount >= 1) {
        this.newItem.quantity += amount;
      }
    },
    deleteFoodItem(index) {
      const itemToDelete = this.foodItems[index];
      http.delete(`/api/foodItems/${itemToDelete.id}`).then(() => {
        this.foodItems.splice(index, 1);
      }).catch(error => {
        this.responseMessage = error.response?.data.message || 'Failed to delete food item.';
      });
    }
  },
  mounted() {
    this.fetchUserData();
    this.fetchFoodItems();
  }
};
</script>
