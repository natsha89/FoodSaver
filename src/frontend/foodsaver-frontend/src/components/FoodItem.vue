/*
* MIT License
* Copyright (c) [2024] [Natasha Shahran]
*
* Permission is granted under the MIT License to use, modify, and distribute
* this software, provided credit is given to the original creator ([Natasha Shahran]).
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
*/

<template>
  <v-container class="my-food-items">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <h2 class="page-title">My Food Items</h2>

        <!-- Alert for expiring food items -->
        <v-alert v-if="alerts.length" :type="alertsType" class="mt-4 mb-4">
          <div v-for="(alert, index) in alerts" :key="index">
            {{ alert }}
          </div>
        </v-alert>

        <v-img
            src="/myfooditems.png"
            alt="My Food Items Image"
            class="my-food-items-image"
            aspect-ratio="2/1"
            contain
        ></v-img>

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

        <!-- Loading indicator -->
        <v-progress-circular v-if="loading" indeterminate color="primary" class="mt-4"></v-progress-circular>

        <!-- Food items list -->
        <v-card class="food-list-card mt-4" v-else-if="foodItems.length">
          <v-card-title class="list-title">Your Food Items</v-card-title>
          <v-divider></v-divider>
          <v-list dense>
            <v-list-item v-for="item in foodItems" :key="item.id" class="food-item-card">
              <v-row align="center">
                <v-col cols="8">
                  <span class="food-item-name">{{ item.name }}</span>
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

        <!-- Empty state -->
        <v-alert v-else type="info" class="mt-4">No food items found.</v-alert>

        <!-- Edit Dialog -->
        <v-dialog v-model="editDialog" max-width="500px">
          <v-card v-if="selectedItem">
            <v-card-title>Edit Food Item</v-card-title>
            <v-card-text>
              <v-text-field v-model="selectedItem.name" label="Name" outlined></v-text-field>
              <v-text-field v-model.number="selectedItem.quantity" type="number" label="Quantity" outlined></v-text-field>
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
      alertsType: 'warning',
      unitOptions: ['gram', 'kg', 'liter', 'pieces'],
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
        const foodItems = this.foodItems;
        this.checkExpiringItems(foodItems);
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
      this.selectedItem = { ...item };
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
    checkExpiringItems(foodItems) {
      const now = new Date();
      const alerts = [];
      foodItems.forEach(item => {
        const expiryDate = new Date(item.expirationDate);
        const diffInDays = (expiryDate - now) / (1000 * 60 * 60 * 24);
        if (diffInDays < 3 && diffInDays > 0) {
          alerts.push({ message: `${item.name} expires in ${Math.ceil(diffInDays)} days!`, type: 'warning' });
        } else if (diffInDays <= 0) {
          alerts.push({ message: `${item.name} has expired!`, type: 'error' });
        }
      });
      this.alerts = alerts.map(alert => alert.message);
      this.alertsType = alerts.length && alerts[0].type === 'error' ? 'error' : 'warning';
    },
  },
};
</script>

<style scoped>
.my-food-items {
  margin-top: 40px;
}
.my-food-items-image {
  border-radius: 8px;
  margin-top: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 70%;
  height: auto;
  margin-left: auto;
  margin-right: auto;
}

.page-title {
  font-size: 2.5em;
  color: #2E7D32;
  text-align: center;
}

.form-card {
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.form-title {
  font-size: 1.3em;
  color: #388E3C;
}

.food-list-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.food-item-card {
  margin-top: 15px;
  padding: 12px;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.list-title {
  font-size: 1.3em;
  color: #4CAF50;
}

.food-item-name {
  font-weight: bold;
  font-size: 1.2em;
  color: #388E3C;
}

.food-item-quantity {
  font-size: 1.1em;
  color: #555;
}

.food-item-expiration {
  color: #FF5722;
  font-size: 0.9em;
}

.v-btn {
  font-size: 0.85em;
}
</style>
