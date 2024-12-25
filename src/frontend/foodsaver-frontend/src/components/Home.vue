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
  <v-container class="home">
    <v-row justify="center" align="center" class="hero">
      <v-col class="text-center">
        <h1 class="headline">Welcome to FoodSaver!</h1>
        <h2 class="slogan">"Reduce Waste, Save Taste"</h2>
        <v-img
            src="/foodsaver-intro.png"
            alt="FoodSaver Intro Image"
            class="intro-image"
        ></v-img>
      </v-col>
    </v-row>

    <v-divider></v-divider>

    <v-row justify="center" class="features">
      <v-col v-for="feature in features" :key="feature.title" cols="12" md="4">
        <v-card class="feature-card" hover>
          <v-img :src="feature.image" height="400px" alt="Feature Image"></v-img>
          <v-card-title class="feature-title">
            <v-icon left>{{ feature.icon }}</v-icon>
            {{ feature.title }}
          </v-card-title>
          <v-card-text>{{ feature.description }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="engagement">
      <v-col>
        <v-card class="engagement-card">
          <v-card-title>Join the FoodWaste Revolution!</v-card-title>
          <v-card-text>
            Start cooking creatively and sustainably with the ingredients you have.
          </v-card-text>
          <v-btn color="green" @click="navigateTo('RecipeGenerator')" class="cta-btn">Start Cooking</v-btn>
          <v-btn color="primary" @click="navigateTo('IngredientList')" large class="cta-btn">
            View My Ingredients
          </v-btn>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      features: [
        {
          title: 'Smart Recipe Generator',
          description: 'Get personalized recipes based on the ingredients you have at home.',
          icon: 'mdi-recipe',
          image: 'SmartRecipeGenerator.png',
        },
        {
          title: 'Expiration Alerts',
          description: 'Receive notifications about ingredients nearing their expiration dates.',
          icon: 'mdi-bell',
          image: 'ExpirationAlerts.png',
        },
        {
          title: 'Sustainable Living',
          description: 'Join the movement towards reducing food waste and promoting sustainability.',
          icon: 'mdi-earth',
          image: 'SustainableLiving.png',
        },
      ],
    };
  },
  methods: {
    navigateTo(page) {
      this.$router.push({name: page});
    },
  },
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 60px;
  background-color: #f9f9f9;
}

.hero {
  padding: 40px 20px;
}

.intro-image {
  height: auto;
  margin: 0 auto 30px;
  max-width: 85%; /* Slightly reduced width */
}

.headline {
  font-size: 3.2em;
  color: #2E7D32;
  font-weight: 700;
  margin-bottom: 15px;
}

.slogan {
  font-size: 1.7em;
  color: #388E3C;
  font-style: italic;
  margin-bottom: 40px;
}

.cta-btn {
  border-radius: 30px;
  font-weight: bold;
  margin: 10px 0;
  padding: 15px 30px; /* Increased padding for better clickability */
  transition: background-color 0.3s ease;
}

.cta-btn:hover {
  background-color: #2E7D32; /* Green hover effect */
}

.feature-card {
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border-radius: 10px; /* Rounded corners for the card */
}

.feature-card:hover {
  transform: scale(1.05);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
}

.feature-title {
  font-size: 1.4em;
  font-weight: 600;
  color: #388E3C;
}

.engagement-card {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.engagement-card v-btn {
  width: 100%;
  padding: 18px 0;
  font-size: 1.1em;
  font-weight: bold;
}
</style>
