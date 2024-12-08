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
        <v-btn color="blue" class="mr-4" @click="navigateTo('IngredientList')" large>
          Handle your groceries
        </v-btn>
      </v-col>
    </v-row>

    <!-- Dialog for expiring food items -->
    <v-dialog v-model="dialogVisible" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">Expiration Alerts</span>
        </v-card-title>
        <v-card-text>
          <v-list>
            <v-list-item v-for="(item, index) in expiringItems" :key="index">
              <v-list-item-content>
                <v-list-item-title>{{ item.name }}</v-list-item-title>
                <v-list-item-subtitle>
                  Expires in {{ item.daysLeft }} days
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card-text>
        <v-card-actions>
          <v-btn color="green" @click="dialogVisible = false">Got it</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  name: 'WelcomeView',
  data() {
    return {
      expiringItems: [], // Store food items that are expiring soon
      dialogVisible: false, // Control the visibility of the dialog
    };
  },
  created() {
    // Check for expiring food items when the component is created
    this.checkExpiringItems();
  },
  watch: {
    // Watch for changes in the store (e.g., after a login)
    '$store.state.isLoggedIn': function(newVal) {
      if (newVal) {
        this.checkExpiringItems(); // Recheck on login
      }
    }
  },
  methods: {
    // Navigate to different pages
    navigateTo(page) {
      this.$router.push({ name: page });
    },

    // Check for expiring food items from the store (or API)
    checkExpiringItems() {
      // Fetch the food items from the store
      const foodItems = this.$store.getters.foodItems;

      // Current date
      const now = new Date();
      const expiringItems = [];

      // Check each food item for expiration
      foodItems.forEach(item => {
        const expiryDate = new Date(item.expirationDate);
        const diffInDays = (expiryDate - now) / (1000 * 60 * 60 * 24); // Difference in days

        if (diffInDays <= 3 && diffInDays >= 0) {
          expiringItems.push({
            name: item.name,
            daysLeft: Math.ceil(diffInDays),
          });
        } else if (diffInDays < 0) {
          expiringItems.push({
            name: item.name,
            daysLeft: 'Expired',
          });
        }
      });

      // If there are any expiring items, show the dialog
      if (expiringItems.length > 0) {
        this.expiringItems = expiringItems;
        this.dialogVisible = true; // Show the dialog
      }
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

.v-dialog .v-card {
  max-width: 500px;
}
</style>
