<template>
  <v-container>
    <!-- Check if user is authenticated -->
    <v-card v-if="isAuthenticated">
      <v-card-title>Profile</v-card-title>
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

        <!-- Show any response message -->
        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>

        <!-- Navigation Buttons -->
        <v-btn @click="goToSavedRecipes" color="primary">My Recipes</v-btn>
        <v-btn @click="goToFoodItems" color="secondary">My Food Items</v-btn>

        <!-- Logout Button -->
        <v-btn @click="logout" color="warning">Logout</v-btn>
      </v-card-text>

      <v-card-actions>
        <!-- Delete Account Button -->
        <v-btn @click="openDeleteDialog" color="red">Delete Account</v-btn>
      </v-card-actions>
    </v-card>

    <!-- If not authenticated, show warning message -->
    <v-alert v-else type="warning">
      You must be logged in to view this page.
      <v-btn @click="goToLogin" color="primary">Login</v-btn>
    </v-alert>

    <!-- Dialog for confirming account deletion -->
    <v-dialog v-model="isDeleteDialogVisible" max-width="400px">
      <v-card>
        <v-card-title class="headline">Confirm Account Deletion</v-card-title>
        <v-card-text>Are you sure you want to delete your account? This action cannot be undone.</v-card-text>
        <v-card-actions>
          <v-btn @click="closeDeleteDialog" color="grey">Cancel</v-btn>
          <v-btn @click="deleteAccount" color="red">Delete</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
import http from '../http'; // Import the centralized HTTP handler

export default {
  name: 'UserProfile',
  computed: {
    ...mapGetters(['isAuthenticated', 'user']),
  },
  data() {
    return {
      responseMessage: '',
      isDeleteDialogVisible: false,  // For controlling delete confirmation dialog
    };
  },
  methods: {
    // Open the confirmation dialog
    openDeleteDialog() {
      this.isDeleteDialogVisible = true;
    },
    // Close the confirmation dialog
    closeDeleteDialog() {
      this.isDeleteDialogVisible = false;
    },
    // Perform the account deletion
    async deleteAccount() {
      try {
        const response = await http.delete('/api/auth/delete'); // Use the http.js instance for API request
        this.responseMessage = response.data.message; // Show the success message
        this.$router.push({ name: 'Home' }); // Redirect user to Home page after account deletion
        this.closeDeleteDialog();  // Close the dialog after deletion
      } catch (error) {
        // Handle error message from backend
        this.responseMessage = error.response?.data?.message || 'An unknown error occurred.';
      }
    },

    // Logout method
    async logout() {
      try {
        this.responseMessage = '';
        // Call the logout API endpoint
        const response = await http.post('/api/auth/logout');
        console.log(response); // or any logic to use the response

        // Cler the authentication state from Vuex
        this.$store.commit('setAuthenticated', false);
        this.$store.commit('setUser', null); // Reset the user data in Vuex store

        // Redirect user to login page after logout
        this.$router.push({ name: 'Login' });
      } catch (error) {
        // Handle any error during logout
        this.responseMessage = error.response?.data?.message || 'An error occurred while logging out.';
      }
    },

    goToSavedRecipes() {
      this.$router.push({ name: 'SavedRecipes' });
    },
    goToFoodItems() {
      this.$router.push({ name: 'FoodItems' });
    },
    goToLogin() {
      this.$router.push({ name: 'Login' }); // Navigate to the login page if not logged in
    },
  },
};
</script>

<style scoped>
/* Add some custom styling if necessary */
</style>
