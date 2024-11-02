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
        <router-link to="/about" class="nav-link">
          <v-btn text>About</v-btn>
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
        <router-link to="/profile" class="nav-link"> <!-- Lägg till denna rad -->
          <v-btn text>My Account</v-btn>
        </router-link>
      </v-toolbar-items>
    </v-toolbar>

    <v-container>
      <router-view></router-view>
    </v-container>
  </v-app>
</template>

<style scoped>
.logo-container {
  display: flex;
  align-items: center; /* Centrera innehållet vertikalt */
  justify-content: flex-start; /* Placera logotypen och texten horisontellt */
}

.logo-image {
  height: 50px; /* Ställ in höjden på logotypen */
  margin-right: 10px; /* Lägger till utrymme mellan logotypen och texten */
}

.title-text {
  font-size: 0.5em; /* Justera textstorleken om det behövs */
  font-weight: bold;
  font-style: italic;
  color: black;
  margin: 0;
}

.overlay-slogan {
  font-size: 0.5em; /* Justera storleken för bättre synlighet */
  font-weight: bold;
  font-style: italic;
  color: black;
  margin-top: 5px;
}

.green-text {
  color: #4caf50; /* Grön färg för texten */
}

.v-toolbar {
  background-color: white;
  color: black;
  margin-top: 20px; /* Justera detta värde om det behövs */
}

.v-toolbar-items .v-btn {
  color: black;
  font-weight: 500;
}

/* Ny klass för länkar */
.nav-link {
  text-decoration: none; /* Ta bort understrykning */
}
</style>

<script>
export default {
  name: 'App',
}
</script>
