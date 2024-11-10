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
        <v-btn color="green" class="mr-4" @click="navigateTo('RecipeGenerator')" large>
          Get Recipes
        </v-btn>
        <v-btn color="secondary" @click="navigateTo('IngredientList')" large>
          View My Ingredients
        </v-btn>
      </v-col>
    </v-row>

    <v-divider></v-divider>

    <v-row justify="center" class="features">
      <v-col v-for="feature in features" :key="feature.title" cols="12" md="4">
        <v-card class="feature-card">
          <v-img :src="feature.image" height="200px" alt="Feature Image"></v-img>
          <v-card-title class="headline">
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
            Start cooking creatively and sustainably with the ingredients you already have. Our mission is to minimize food waste while maximizing flavor.
          </v-card-text>
          <v-btn color="green" @click="navigateTo('RecipeGenerator')">Start Cooking</v-btn>
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
      this.$router.push({ name: page });
    },
  },
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 80px;
  background-color: #f9f9f9;
}

.hero {
  padding: 20px;
}

.intro-image {
  max-width: 600px;
  height: auto;
  margin: 0 auto 20px;
}

.headline {
  font-size: 2.5em;
  color: #2E7D32; /* Darker green for the headline */
}

.slogan {
  font-size: 1.5em;
  color: #388E3C; /* Lighter green for the slogan */
  margin-bottom: 20px;
}

.feature-card {
  transition: transform 0.2s;
}
.feature-card:hover {
  transform: scale(1.05);
}

.engagement {
  margin-top: 40px;
}
.engagement-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
