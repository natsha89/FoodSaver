<template>
  <v-app>
    <v-toolbar app class="custom-toolbar">
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
      <v-toolbar-items>
        <router-link to="/" class="nav-link">
          <v-btn text>Home</v-btn>
        </router-link>
        <router-link to="/ingredient-list" class="nav-link">
          <v-btn text>Food Items</v-btn>
        </router-link>
        <router-link to="/recipe-generator" class="nav-link">
          <v-btn text>Recipe Generator</v-btn>
        </router-link>
        <router-link to="/saved-recipes" class="nav-link">
          <v-btn text>Saved Recipes</v-btn>
        </router-link>
        <router-link to="/login" class="nav-link">
          <v-btn text>Sign In</v-btn>
        </router-link>
        <router-link to="/profile" class="nav-link">
          <v-btn text>My Account</v-btn>
        </router-link>
      </v-toolbar-items>
    </v-toolbar>

      <router-view></router-view>
  </v-app>
</template>

<style scoped>
.logo-container {
  display: flex;
  align-items: center; /* Vertically center the content */
  justify-content: flex-start; /* Horizontally position logo and text */
}

.logo-image {
  height: 30px; /* Further decrease logo height */
  margin-right: 5px; /* Reduce space between logo and text */
}

.title-text {
  font-size: 0.35em; /* Further reduce text size */
  font-weight: bold;
  font-style: italic;
  color: black;
  margin: 0;
}

.overlay-slogan {
  font-size: 0.35em; /* Further reduce size for better visibility */
  font-weight: bold;
  font-style: italic;
  color: black;
  margin-top: 2px; /* Reduce margin */
}

.green-text {
  color: #4caf50; /* Green color for text */
}

.v-toolbar {
  background-color: white;
  color: black;
  min-height: 40px; /* Set an even smaller minimum height */
  padding: 4px 8px; /* Further reduce padding */
}

.v-toolbar-items .v-btn {
  color: black;
  font-weight: 500;
  font-size: 0.8em; /* Further reduce button font size */
}

/* New class for links */
.nav-link {
  text-decoration: none; /* Remove underline */
}
</style>

<script>
export default {
  name: 'App',
}
</script>
