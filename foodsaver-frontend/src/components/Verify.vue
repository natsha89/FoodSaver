<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title>
        <div class="logo-container">
          <img :src="require('@/assets/logo.png')" alt="FoodSaver Logo" class="logo-image" />
          <div class="title-text-container">
            <span class="title-text">
              Food<span class="green-text">Saver</span> -
            </span>
            <span class="overlay-slogan">
              <span class="green-text">Reduce</span> Waste,
              <span class="green-text">Save</span> <span class="green-text">Taste</span>
            </span>
          </div>
        </div>
      </v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>

    <v-container>
      <v-card>
        <v-card-title>Email Verified!</v-card-title>
        <v-card-text>
          <p>Congratulations, your email has been verified! You are now ready to start using the service. Please log in.</p>
          <router-link to="/login">
            <v-btn>Go to Login</v-btn>
          </router-link>
        </v-card-text>
      </v-card>
    </v-container>

  </v-app>
</template>

<script>
export default {
  name: 'VerificationSuccess',
};
</script>

<style scoped>
/* Optional: Add any specific styles for this page */
</style>
