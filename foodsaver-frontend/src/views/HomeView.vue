<template>
  <v-container class="home">
    <v-row justify="center" align="center" class="hero">
      <v-col class="text-center">
        <v-img
            src="/foodsaver-intro.png"
            alt="FoodSaver Intro Image"
            class="intro-image"
        ></v-img>
        <h1>Welcome to FoodSaver!</h1>
        <h2 class="slogan">"Reduce Waste, Save Taste"</h2>

        <v-card class="intro-card">
          <v-card-text>
            FoodSaver is here to help you minimize food waste by making the most of what you already have in your kitchen. Let us help you turn leftovers into delicious meals!
          </v-card-text>
        </v-card>

        <v-btn color="green" class="mr-4" @click="navigateTo('RecipeGenerator')">Get Recipes</v-btn>
      </v-col>
    </v-row>

    <v-divider></v-divider>
    <v-row>
      <v-col v-for="feature in features" :key="feature.title" cols="12" md="4">
        <v-card class="feature-card">
          <v-card-title class="headline">{{ feature.title }}</v-card-title>
          <v-card-text>{{ feature.description }}</v-card-text>
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
        },
        {
          title: 'Expiration Alerts',
          description: 'Receive notifications about ingredients nearing their expiration dates.',
        },
        {
          title: 'Sustainable Living',
          description: 'Join the movement towards reducing food waste and promoting sustainability.',
        },
      ],
    };
  },
  methods: {
    navigateTo(page) {
      this.$router.push({ name: page }); // Navigates to the specified page
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
.intro-image {
  max-width: 700px; /* Justera bredden på bilden */
  height: auto; /* Bibehåll proportioner */
  margin: 0 auto 20px; /* Centrera och lägg till marginal under bilden */
}
.intro-card {
  margin: 20px auto;
  max-width: 500px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.feature-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}
.feature-card:hover {
  transform: scale(1.05);
}
.slogan {
  font-size: 1.5em;
  color: #2E7D32;
}
</style>
