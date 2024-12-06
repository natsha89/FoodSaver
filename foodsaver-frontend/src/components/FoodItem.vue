<template>
  <v-container class="my-food-items">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <h2 class="page-title">My Food Items</h2>

        <v-card class="form-card">
          <v-card-title class="form-title">Add a New Food Item</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="createFoodItem">
              <v-row dense>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newFoodItem.name" label="Name" outlined required></v-text-field>
                </v-col>
                <v-col cols="6" md="3">
                  <v-text-field v-model.number="newFoodItem.quantity" type="number" label="Quantity" outlined required></v-text-field>
                </v-col>
                <v-col cols="6" md="3">
                  <!-- Updated v-select for Unit -->
                  <v-select
                      v-model="newFoodItem.unit"
                      :items="unitOptions"
                      label="Unit"
                      outlined
                      required
                  ></v-select>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="newFoodItem.expirationDate" type="date" label="Expiration Date" outlined></v-text-field>
                </v-col>
              </v-row>
              <v-btn type="submit" color="green" block>Add Food Item</v-btn>
            </v-form>
          </v-card-text>
        </v-card>

        <v-alert v-if="alerts.length" type="warning" class="mt-4">
          <div v-for="(alert, index) in alerts" :key="index">
            {{ alert }}
          </div>
        </v-alert>

        <v-progress-circular v-if="loading" indeterminate color="primary" class="mt-4"></v-progress-circular>

        <v-card class="food-list-card mt-4" v-else-if="foodItems.length">
          <v-card-title class="list-title">Your Food Items</v-card-title>
          <v-divider></v-divider>
          <v-list dense>
            <v-list-item v-for="item in foodItems" :key="item.id">
              <v-row>
                <v-col cols="8">
                  <span class="food-item-name">{{ item.name }}</span> -
                  <span class="food-item-quantity">{{ item.quantity }} {{ item.unit }}</span>
                  <br>
                  <small class="food-item-expiration">Expires: {{ formatDate(item.expirationDate) }}</small>
                </v-col>
                <v-col cols="4" class="text-right">
                  <v-btn @click="openEditDialog(item)" small color="primary" outlined>Edit</v-btn>
                  <v-btn @click="confirmDelete(item.id)" small color="error" outlined>Delete</v-btn>
                </v-col>
              </v-row>
            </v-list-item>
          </v-list>
        </v-card>
        <v-alert v-else type="info" class="mt-4">No food items found.</v-alert>

        <!-- Edit Dialog -->
        <v-dialog v-model="editDialog" max-width="500px">
          <v-card v-if="selectedItem">
            <v-card-title>Edit Food Item</v-card-title>
            <v-card-text>
              <v-text-field v-model="selectedItem.name" label="Name" outlined></v-text-field>
              <v-text-field v-model.number="selectedItem.quantity" type="number" label="Quantity" outlined></v-text-field>
              <!-- Updated v-select for Unit -->
              <v-select
                  v-model="selectedItem.unit"
                  :items="unitOptions"
                  label="Unit"
                  outlined
              ></v-select>
              <v-text-field v-model="selectedItem.expirationDate" type="date" label="Expiration Date" outlined></v-text-field>
            </v-card-text>
            <v-card-actions>
              <v-btn @click="updateFoodItem" color="green">Save</v-btn>
              <v-btn @click="editDialog = false" color="secondary">Cancel</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      editDialog: false,
      selectedItem: null,
      alerts: [],
      unitOptions: ['gram', 'kg', 'liter', 'pieces'], // Predefined list of units
      newFoodItem: {
        name: '',
        quantity: null,
        unit: '',
        expirationDate: null,
      },
    };
  },
  computed: {
    foodItems() {
      return this.$store.getters.foodItems;
    },
  },
  created() {
    this.loadFoodItems();
  },
  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString() : 'N/A';
    },
    async loadFoodItems() {
      try {
        this.loading = true;
        await this.$store.dispatch('fetchFoodItems');
      } catch (error) {
        this.$toast.error('Failed to load food items');
      } finally {
        this.loading = false;
      }
    },
    async createFoodItem() {
      try {
        const response = await this.$store.dispatch('createFoodItem', this.newFoodItem);
        this.alerts = response.alerts || [];
        this.newFoodItem = { name: '', quantity: null, unit: '', expirationDate: null };
        await this.loadFoodItems();
        this.$toast.success('Food item created');
      } catch (error) {
        this.$toast.error('Failed to create food item');
      }
    },
    async updateFoodItem() {
      try {
        const response = await this.$store.dispatch('updateFoodItem', {
          id: this.selectedItem.id,
          foodItem: this.selectedItem,
        });
        this.alerts = response.alerts || [];
        this.editDialog = false;
        await this.loadFoodItems();
        this.$toast.success('Food item updated');
      } catch (error) {
        this.$toast.error('Failed to update food item');
      }
    },
    openEditDialog(item) {
      this.selectedItem = {...item};
      this.editDialog = true;
    },
    confirmDelete(foodItemId) {
      if (confirm('Are you sure you want to delete this food item?')) {
        this.deleteFoodItem(foodItemId);
      }
    },
    async deleteFoodItem(foodItemId) {
      try {
        await this.$store.dispatch('deleteFoodItem', foodItemId);
        this.$toast.success('Food item deleted');
      } catch (error) {
        this.$toast.error('Failed to delete food item');
      }
    },
  },
};
</script>

<style scoped>
.my-food-items {
  margin-top: 40px;
}

.page-title {
  font-size: 2em;
  color: #2E7D32;
  text-align: center;
}

.form-card {
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 1.2em;
  color: #388E3C;
}

.food-list-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.list-title {
  font-size: 1.3em;
  color: #4CAF50;
}

.food-item-name {
  font-weight: bold;
}

.food-item-expiration {
  color: #FF5722;
}
</style>