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
  <v-app>
    <v-app-bar app class="custom-toolbar">
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>
        <div class="logo-container">
          <img :src="require('@/assets/logo.png')" alt="FoodSaver Logo" class="logo-image" />
          <div class="title-text-container">
            <span class="title-text">
              Food<span class="green-text">Saver</span>
            </span>
            <span class="overlay-slogan">
              <span class="green-text">Reduce</span> Waste, <span class="green-text">Save</span> <span>Taste</span>
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
        <v-btn text @click="logout" class="nav-btn" color="red">Log Out</v-btn>
      </v-toolbar-items>
    </v-app-bar>
    <v-navigation-drawer app v-model="drawer" class="custom-drawer">
      <router-link to="/" class="nav-link">
        <v-list-item link>Home</v-list-item>
      </router-link>
      <router-link to="/ingredient-list" class="nav-link">
        <v-list-item link>Food Items</v-list-item>
      </router-link>
      <router-link to="/recipe-generator" class="nav-link">
        <v-list-item link>Recipe Generator</v-list-item>
      </router-link>
      <router-link to="/saved-recipes" class="nav-link">
        <v-list-item link>Saved Recipes</v-list-item>
      </router-link>
      <router-link to="/login" class="nav-link">
        <v-list-item link>Log In</v-list-item>
      </router-link>
      <router-link to="/profile" class="nav-link">
        <v-list-item link>My Account</v-list-item>
      </router-link>
      <v-list-item @click="logout" link>Log Out</v-list-item>
    </v-navigation-drawer>
    <router-view></router-view>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      drawer: false,
    };
  },
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
  padding: 0 8px; /* Reduced padding */
  min-height: 48px; /* Reduced height */
}
.logo-container {
  display: flex;
  align-items: center;
  gap: 8px; /* Reduced gap between logo and text */
}
.logo-image {
  height: 30px; /* Reduced logo size */
  margin-right: 4px; /* Reduced margin */
}
.title-text-container {
  display: flex;
  flex-direction: column;
}
.title-text {
  font-size: 0.9em; /* Reduced font size */
  font-weight: bold;
  color: #333;
}
.overlay-slogan {
  font-size: 0.6em; /* Reduced font size */
  font-weight: bold;
  color: #4CAF50;
}
.nav-btn {
  font-weight: 600;
  color: #333;
  font-size: 0.8em; /* Reduced font size for buttons */
}
.toolbar-buttons {
  display: flex;
  align-items: center;
  gap: 8px; /* Reduced gap between buttons */
}
.nav-link {
  text-decoration: none;
}
.custom-drawer {
  background-color: white;
  color: black;
  min-height: 100%;
  padding-top: 10px;
}
</style>
