<template>
  <v-container class="about">
    <v-row>
      <v-col>
        <h1>Welcome to <span class="black-text">Food</span><span class="green-text">Saver</span>!</h1>
        <v-divider></v-divider>

        <v-card>
          <v-card-title class="headline"><span class="black-text">About</span> <span class="black-text">Food</span><span class="green-text">Saver</span></v-card-title>
          <v-card-text>
            <span class="black-text">Food</span><span class="green-text">Saver</span> is an innovative AI-powered meal planner that helps you <strong><span class="green-text">minimize food waste</span></strong> and create delicious meals with the food items you already have at home.
          </v-card-text>
          <v-card-text>
            By analyzing your available food items, we provide creative recipe suggestions that make it easier to cook tasty meals while contributing to a more sustainable lifestyle.
          </v-card-text>
          <v-card-text>
            Our slogan <strong>"<span class="green-text">Reduce</span> <span class="black-text">Waste</span>, <span class="green-text">Save</span> <span class="black-text">Taste</span>"</strong> encapsulates our mission: to encourage responsible consumption and help you make the most of what you have.
          </v-card-text>
        </v-card>

        <v-divider></v-divider>
        <v-card>
          <v-card-title class="headline">Make a Difference Today!</v-card-title>
          <v-card-text>
            Use <span class="black-text">Food</span><span class="green-text">Saver</span> to:
            <ul>
              <li>Get insights into which food items are nearing their expiration dates.</li>
              <li>Discover new ways to use your leftovers.</li>
              <li>Contribute to a more sustainable world by <strong><span class="green-text">reducing</span></strong> food <span class="black-text">waste</span>.</li>
            </ul>
          </v-card-text>
        </v-card>

        <v-divider></v-divider>
        <v-card>
          <v-card-text class="slogan">
            <div class="slogan-container">
              <span class="foodsaver-title">
                <span class="black-text">Food</span><span class="green-text">Saver</span>
              </span>
              <br />
              <span class="reduce-save-slogan">
                <span class="green-text"><strong>Reduce</strong></span> <span class="black-text">Waste</span>,
                <span class="green-text">Save</span> <span class="black-text">Taste</span>
              </span>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'AboutView',
}
</script>

<style scoped>
.about {
  text-align: center;
  margin-top: 20px;
  background-color: #f9f9f9;
}
.headline {
  color: #388E3C;
}
.slogan {
  font-size: 1.5em; /* Adjust font size as needed */
  color: #2E7D32;
  margin-top: 20px; /* Add space above the slogan */
  text-align: center; /* Center-align text */
}
.slogan-container {
  padding: 10px; /* Optional: add padding for space around slogan */
}
.v-divider {
  background-color: #388E3C;
}
.v-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.green-text {
  color: #4caf50; /* Green color for "Saver" */
}
.black-text {
  color: #000000; /* Black color for other text */
}
</style>
