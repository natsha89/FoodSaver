<template>
  <div>
    <h2>My Food Items</h2>

    <v-form @submit.prevent="createFoodItem">
      <v-text-field
          v-model="newFoodItem.name"
          label="Name"
          required
      ></v-text-field>
      <v-text-field
          v-model.number="newFoodItem.quantity"
          type="number"
          label="Quantity"
          required
      ></v-text-field>
      <v-text-field
          v-model="newFoodItem.unit"
          label="Unit"
          required
      ></v-text-field>
      <v-text-field
          v-model="newFoodItem.expirationDate"
          type="date"
          label="Expiration Date"
      ></v-text-field>
      <v-btn type="submit" color="primary">Add Food Item</v-btn>
    </v-form>

    <v-alert v-if="alerts.length" type="warning">
      <div v-for="(alert, index) in alerts" :key="index">
        {{ alert }}
      </div>
    </v-alert>

    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>

    <v-list v-else-if="foodItems.length">
      <v-list-item v-for="item in foodItems" :key="item.id">
        <v-row>
          <v-col cols="8">
            {{ item.name }} - {{ item.quantity }} {{ item.unit }}
            <br>
            Expires: {{ formatDate(item.expirationDate) }}
          </v-col>
          <v-col cols="4">
            <v-btn @click="openEditDialog(item)" small color="primary">Edit</v-btn>
            <v-btn @click="confirmDelete(item.id)" small color="error">Delete</v-btn>
          </v-col>
        </v-row>
      </v-list-item>
    </v-list>
    <p v-else>No food items found.</p>

    <v-dialog v-model="editDialog" max-width="500px">
      <v-card v-if="selectedItem">
        <v-card-title>Edit Food Item</v-card-title>
        <v-card-text>
          <v-text-field v-model="selectedItem.name" label="Name"></v-text-field>
          <v-text-field
              v-model.number="selectedItem.quantity"
              type="number"
              label="Quantity"
          ></v-text-field>
          <v-text-field v-model="selectedItem.unit" label="Unit"></v-text-field>
          <v-text-field
              v-model="selectedItem.expirationDate"
              type="date"
              label="Expiration Date"
          ></v-text-field>
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
      alerts: [],
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

        this.newFoodItem = {
          name: '',
          quantity: null,
          unit: '',
          expirationDate: null
        };

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
          foodItem: this.selectedItem
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
    }
  }
};
</script>