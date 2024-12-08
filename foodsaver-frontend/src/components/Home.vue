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
  margin-top: 80px;
  background-color: #f9f9f9;
}

.hero {
  padding: 20px;
}

.intro-image {
  height: auto;
  margin: 0 auto 20px;
  max-width: 50%; /* Reduce the width of the image */

}

.headline {
  font-size: 3em;
  color: #2E7D32;
  font-weight: bold;
  margin-bottom: 10px;
}

.slogan {
  font-size: 1.6em;
  color: #388E3C;
  font-style: italic;
  margin-bottom: 30px;
}

.cta-btn {
  border-radius: 25px;
  font-weight: bold;
}

.feature-card {
  transition: transform 0.2s, box-shadow 0.3s;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.feature-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.feature-title {
  font-size: 1.3em;
  font-weight: bold;
  color: #388E3C;
}

.engagement-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.engagement-card v-btn {
  width: 100%;
  padding: 15px;
}
</style>
