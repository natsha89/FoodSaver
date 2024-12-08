<template>
  <v-app>
    <v-toolbar app class="custom-toolbar">
      <v-toolbar-title>
        <div class="logo-container">
          <img :src="require('@/assets/logo.png')" alt="FoodSaver Logo" class="logo-image" />
          <div class="title-text-container">
            <span class="title-text">
              Food<span class="green-text">Saver</span>
            </span>
            <span class="overlay-slogan">
              <span class="green-text">Reduce</span> Waste,
              <span class="green-text">Save</span> <span>Taste</span>
            </span>
          </div>
        </div>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items class="toolbar-buttons">
        <router-link to="/" class="nav-link">
          <v-btn text class="nav-btn">Home</v-btn>
        </router-link>
        <router-link to="/ingredient-list" class="nav-link">
          <v-btn text class="nav-btn">Food Items</v-btn>
        </router-link>
        <router-link to="/recipe-generator" class="nav-link">
          <v-btn text class="nav-btn">Recipe Generator</v-btn>
        </router-link>
        <router-link to="/saved-recipes" class="nav-link">
          <v-btn text class="nav-btn">Saved Recipes</v-btn>
        </router-link>
        <router-link to="/login" class="nav-link">
          <v-btn text class="nav-btn">Log In</v-btn>
        </router-link>
        <router-link to="/profile" class="nav-link">
          <v-btn text class="nav-btn">My Account</v-btn>
        </router-link>

        <!-- Röd Logout-knapp -->
        <v-btn text @click="logout" class="nav-btn" color="red">Log Out</v-btn>
      </v-toolbar-items>
    </v-toolbar>

    <router-view></router-view>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  methods: {
    logout() {
      this.$store
          .dispatch('logout') // Call Vuex logout action
          .then(() => {
            this.$router.push('/login'); // Redirect to login page
          })
          .catch((error) => {
            console.error('Logout failed:', error);
            alert('An error occurred during logout.');
          });
    },
  },
};
</script>

<style scoped>
/* Styling for Toolbar */
.v-toolbar {
  background-color: white;
  color: black;
  padding: 0 8px;  /* Reduced padding */
  min-height: 48px;  /* Reduced height */
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 8px; /* Reduced gap between logo and text */
}

.logo-image {
  height: 30px;  /* Reduced logo size */
  margin-right: 4px; /* Reduced margin */
}

.title-text-container {
  display: flex;
  flex-direction: column;
}

.title-text {
  font-size: 0.9em;  /* Reduced font size */
  font-weight: bold;
  color: #333;
}

.overlay-slogan {
  font-size: 0.6em;  /* Reduced font size */
  font-weight: bold;
  color: #4CAF50;
}

.nav-btn {
  font-weight: 600;
  color: #333;
  font-size: 0.8em;  /* Reduced font size for buttons */
}

.toolbar-buttons {
  display: flex;
  align-items: center;
  gap: 8px;  /* Reduced gap between buttons */
}

.nav-link {
  text-decoration: none;
}

</style>
