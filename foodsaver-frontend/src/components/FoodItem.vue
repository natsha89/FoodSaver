<template>
  <div>
    <h2>My Food Items</h2>
    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>
    <v-list v-else-if="foodItems.length">
      <v-list-item v-for="item in foodItems" :key="item.id">
        {{ item.name }} - Quantity: {{ item.quantity }} {{ item.unit }}
      </v-list-item>
    </v-list>
    <p v-else>No food items found.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true
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
    async loadFoodItems() {
      try {
        this.loading = true;
        await this.$store.dispatch('fetchFoodItems');
      } catch (error) {
        console.error('Failed to load food items:', error);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>
