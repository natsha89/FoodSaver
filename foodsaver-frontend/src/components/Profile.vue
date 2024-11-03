<template>
  <v-container>
    <v-card v-if="isAuthenticated">
      <v-card-title>My Account</v-card-title>
      <v-card-text>
        <v-list>
          <v-list-item>
            <v-list-item-title>Full Name: {{ user.fullName }}</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>Email: {{ user.email }}</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>Allergies: {{ user.allergies.join(', ') || 'None' }}</v-list-item-title>
          </v-list-item>
        </v-list>

        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>

        <v-btn @click="goToSavedRecipes" color="primary">My Recipes</v-btn>
        <v-btn @click="goToFoodItems" color="secondary">My Food Items</v-btn>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="deleteAccount" color="red">Delete Account</v-btn>
      </v-card-actions>
    </v-card>

    <v-alert v-else type="warning">
      You must be logged in to view this page.
      <v-btn @click="goToLogin" color="primary">Login</v-btn> <!-- Button to go to Login page -->
    </v-alert>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'; // Import Vuex mapGetters

export default {
  name: 'UserProfile',
  computed: {
    ...mapGetters(['isAuthenticated', 'user']), // Map getters from Vuex
  },
  data() {
    return {
      responseMessage: '',
    };
  },
  methods: {
    async deleteAccount() {
      const confirmed = confirm('Are you sure you want to delete your account? This action cannot be undone.');
      if (confirmed) {
        try {
          const response = await this.$http.delete('/api/auth/delete');
          this.responseMessage = response.data.message;
          this.$router.push({name: 'Home'});
        } catch (error) {
          this.responseMessage = error.response.data.message || 'An unknown error occurred.';
        }
      }
    },
    goToSavedRecipes() {
      this.$router.push({name: 'SavedRecipes'});
    },
    goToFoodItems() {
      this.$router.push({name: 'FoodItems'});
    },
    goToLogin() {
      this.$router.push({name: 'Login'}); // Navigate to the login page
    },
  },
};
</script>

<style scoped>
</style>
