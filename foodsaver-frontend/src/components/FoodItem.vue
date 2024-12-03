<template>
  <div>
    <h2>My Food Items</h2>

    <!-- Create Food Item Form -->
    <v-form @submit.prevent="createFoodItem">
      <v-text-field v-model="newFoodItem.name" label="Name" required></v-text-field>
      <v-text-field v-model="newFoodItem.quantity" type="number" label="Quantity" required></v-text-field>
      <v-text-field v-model="newFoodItem.unit" label="Unit" required></v-text-field>
      <v-text-field v-model="newFoodItem.expirationDate" type="date" label="Expiration Date"></v-text-field>
      <v-btn type="submit" color="primary">Add Food Item</v-btn>
    </v-form>

    <!-- Alerts Display -->
    <v-alert v-if="currentAlerts.length" type="warning">
      <div v-for="(alert, index) in currentAlerts" :key="index">
        {{ alert }}
      </div>
    </v-alert>

    <!-- Loading Indicator -->
    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>

    <!-- Food Items List -->
    <v-list v-else-if="foodItems.length">
      <v-list-item v-for="item in foodItems" :key="item.id">
        <v-row>
          <v-col cols="8">
            {{ item.name }} - Quantity: {{ item.quantity }} {{ item.unit }}
            <br>
            Expires: {{ item.expirationDate }}
          </v-col>
          <v-col cols="4">
            <v-btn @click="openEditDialog(item)" small color="primary">Edit</v-btn>
            <v-btn @click="deleteFoodItem(item.id)" small color="error">Delete</v-btn>
          </v-col>
        </v-row>
      </v-list-item>
    </v-list>
    <p v-else>No food items found.</p>

    <!-- Edit Food Item Dialog -->
    <v-dialog v-model="editDialog" max-width="500px">
      <v-card v-if="selectedItem">
        <v-card-title>Edit Food Item</v-card-title>
        <v-card-text>
          <v-text-field v-model="selectedItem.name" label="Name"></v-text-field>
          <v-text-field v-model="selectedItem.quantity" type="number" label="Quantity"></v-text-field>
          <v-text-field v-model="selectedItem.unit" label="Unit"></v-text-field>
          <v-text-field v-model="selectedItem.expirationDate" type="date" label="Expiration Date"></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="updateFoodItem" color="primary">Save</v-btn>
          <v-btn @click="editDialog = false" color="secondary">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      editDialog: false,
      selectedItem: null,
      currentAlerts: [],
      newFoodItem: {
        name: '',
        quantity: null,
        unit: '',
        expirationDate: null
      }
    };
  },
  computed: {
    foodItems() {
      return this.$store.getters.foodItems;
    }
  },
  created() {
    this.loadFoodItems();
  },
  methods: {
    // Load all food items for the current user
    async loadFoodItems() {
      try {
        this.loading = true;
        await this.$store.dispatch('fetchFoodItems');
      } catch (error) {
        console.error('Failed to load food items:', error);
        this.$toast.error('Failed to load food items');
      } finally {
        this.loading = false;
      }
    },

    // Create a new food item
    async createFoodItem() {
      try {
        // Call Vuex action with new food item data
        const response = await this.$store.dispatch('createFoodItem', this.newFoodItem);

        // Check for alerts from backend
        if (response.alerts && response.alerts.length) {
          this.currentAlerts = response.alerts;
        }

        // Reset form fields
        this.newFoodItem = {
          name: '',
          quantity: null,
          unit: '',
          expirationDate: null
        };

        // Refresh food items list
        await this.loadFoodItems();

        // Show success toast
        this.$toast.success('Food item created successfully');
      } catch (error) {
        console.error('Failed to create food item:', error);
        this.$toast.error('Failed to create food item');
      }
    },

    // Update an existing food item
    async updateFoodItem() {
      try {
        // Call the Vuex action with id and updated item
        const response = await this.$store.dispatch('updateFoodItem', {
          id: this.selectedItem.id,
          foodItem: this.selectedItem
        });

        // Check for alerts
        if (response.alerts && response.alerts.length) {
          this.currentAlerts = response.alerts;
        }

        // Close the edit dialog
        this.editDialog = false;

        // Refresh food items list
        await this.loadFoodItems();

        // Optional: Show success toast
        this.$toast.success('Food item updated successfully');
      } catch (error) {
        console.error('Failed to update food item:', error);
        this.$toast.error('Failed to update food item');
      }
    },
    // Delete a food item
    async deleteFoodItem(foodItemId) {
      try {
        await this.$store.dispatch('deleteFoodItem', foodItemId);
        this.$toast.success('Food item deleted successfully');
      } catch (error) {
        console.error('Failed to delete food item:', error);
        this.$toast.error('Failed to delete food item');
      }
    }
  }
};
</script>