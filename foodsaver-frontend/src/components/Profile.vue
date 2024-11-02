<template>
  <v-container>
    <v-card>
      <v-card-title>My Account</v-card-title>
      <v-card-text>
        <!-- Användarinformation kan visas här -->
        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>
        <v-btn @click="goToSavedRecipes" color="primary">My Recipes</v-btn> <!-- Genväg till My Recipes -->
      </v-card-text>
      <v-card-actions>
        <v-btn @click="deleteAccount" color="red">Delete Account</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'UserProfile', // Ändrat från Profile till UserProfile
  data() {
    return {
      responseMessage: '', // Variabel för att lagra responsmeddelanden
    };
  },
  methods: {
    async deleteAccount() {
      const confirmed = confirm('Are you sure you want to delete your account? This action cannot be undone.');
      if (confirmed) {
        try {
          const response = await this.$http.delete('/api/auth/delete');
          this.responseMessage = response.data.message; // Visa framgångsmeddelande
          // Eventuellt omdirigera användaren eller logga ut efter borttagning
        } catch (error) {
          this.responseMessage = error.response.data.message || 'An unknown error occurred.'; // Hantera felrespons
        }
      }
    },
    goToSavedRecipes() {
      this.$router.push({ name: 'SavedRecipes' }); // Navigera till Saved Recipes
    },
  },
};
</script>

<style scoped>
</style>
