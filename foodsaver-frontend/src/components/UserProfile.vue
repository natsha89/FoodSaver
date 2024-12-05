<template>
  <div>
    <h2>My Account</h2>

    <!-- Loading Spinner -->
    <v-progress-circular v-if="loading" indeterminate></v-progress-circular>

    <!-- User Details -->
    <div v-else-if="user" class="user-details">
      <v-card class="pa-4 mb-4">
        <v-data-table
            :items="[user]"
            :headers="headers"
            class="elevation-1"
            dense
        >
          <template v-slot:[`item.allergies`]="{ item }">
            {{ item.allergies && item.allergies.length ? item.allergies.join(', ') : 'None' }}
          </template>
          <template v-slot:[`item.dietaryPreferences`]="{ item }">
            {{ item.dietaryPreferences && item.dietaryPreferences.length ? item.dietaryPreferences.join(', ') : 'None' }}
          </template>
        </v-data-table>
      </v-card>

      <v-card-actions>
        <!-- Edit Profile Button -->
        <v-btn @click="openEditDialog" color="primary">Edit Profile</v-btn>

        <!-- My Food Items Button -->
        <v-btn @click="goToFoodItems" color="secondary">My Food Items</v-btn>

        <!-- My Recipes Button -->
        <v-btn @click="goToRecipes" color="secondary">My Recipes</v-btn>

        <!-- Generate Recipe Button -->
        <v-btn @click="goToRecipeGenerator" color="primary">Generate Recipe</v-btn>
      </v-card-actions>
    </div>

    <p v-else>No user data found.</p>

    <!-- Edit Profile Dialog -->
    <v-dialog v-model="editDialog" max-width="500px">
      <v-card v-if="selectedItem">
        <v-card-title>Edit Profile</v-card-title>
        <v-card-text>
          <v-text-field v-model="selectedItem.fullName" label="Full Name"></v-text-field>
          <v-text-field v-model="selectedItem.email" label="Email"></v-text-field>
          <v-textarea
              v-model="selectedItem.allergies"
              label="Allergies (comma-separated)"
          ></v-textarea>
          <v-textarea
              v-model="selectedItem.dietaryPreferences"
              label="Dietary Preferences (comma-separated)"
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="updateUser" color="primary">Save</v-btn>
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
      headers: [
        {text: 'Full Name', value: 'fullName'},
        {text: 'Email', value: 'email'},
        {text: 'Allergies', value: 'allergies'},
        {text: 'Dietary Preferences', value: 'dietaryPreferences'}
      ]
    };
  },
  computed: {
    user() {
      return this.$store.getters.user; // Fetch user data from Vuex store
    }
  },
  created() {
    this.loadUser(); // Load user data when component is created
  },
  methods: {
    async loadUser() {
      try {
        this.loading = true;
        await this.$store.dispatch('fetchUser'); // Fetch user from Vuex store
      } catch (error) {
        console.error('Failed to load user', error);
        this.$toast.error(`Failed to load user: ${error.message}`);
      } finally {
        this.loading = false;
      }
    },
    openEditDialog() {
      this.selectedItem = {...this.user}; // Copy user data for editing
      this.editDialog = true;
    },
    async updateUser() {
      try {
        await this.$store.dispatch('updateUser', this.selectedItem); // Update user via Vuex action
        this.editDialog = false;
        this.$toast.success('User updated successfully');
      } catch (error) {
        this.$toast.error('Failed to update user');
      }
    },
    goToFoodItems() {
      this.$router.push('/fooditems'); // Navigate to the Food Items page
    },
    goToRecipes() {
      this.$router.push('/recipes'); // Navigate to the Recipes page
    },
    goToRecipeGenerator() {
      this.$router.push('/recipe-generator'); // Navigate to the Recipe Generator page
    }
  }
};
</script>

<style scoped>
/* Optional: Add custom styles for the profile page */
</style>
