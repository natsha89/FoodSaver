<template>
  <v-container class="welcome">
    <v-row justify="center" align="center">
      <v-col class="text-center">
        <h1>Welcome to your FoodSaver world!</h1>
        <v-img
            src="/welcome.png"
            alt="Welcome Image"
            class="welcome-image"
        ></v-img>
        <h2>Start your recipe collection</h2>
        <v-btn color="green" class="mr-4" @click="navigateTo('RecipeGenerator')" large>
          Get Recipes
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'WelcomeView',
  data() {
    return {
      userName: 'User', // Replace with dynamic user name fetched from login or state
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
.welcome {
  text-align: center;
  margin-top: 80px;
}

.welcome-image {
  max-width: 800px;
  height: auto;
  margin: 20px auto;
}

h1 {
  font-size: 2.2em;
  color: #2E7D32;
}

h2 {
  font-size: 1.4em;
  color: #388E3C;
}
</style>
