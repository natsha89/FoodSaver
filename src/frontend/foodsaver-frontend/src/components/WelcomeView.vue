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
  <v-container class="welcome">
    <v-row justify="center" align="center" class="welcome-row">
      <v-col class="text-center">
        <h1 class="welcome-title">Welcome to Your FoodSaver World!</h1>
        <v-img
            src="/welcome.png"
            alt="Welcome Image"
            class="welcome-image"
        ></v-img>
        <h2 class="subheading">Start your recipe collection or add your groceries!</h2>
        <div class="cta-buttons">
          <v-btn color="success" class="cta-btn" @click="navigateTo('RecipeGenerator')" large>
            Get Recipes
          </v-btn>
          <v-btn color="primary" class="cta-btn" @click="navigateTo('IngredientList')" large>
            Manage Your Groceries
          </v-btn>
        </div>
      </v-col>
    </v-row>

    <!-- Dialog for expiring food items -->
    <v-dialog v-model="dialogVisible" max-width="600px">
      <v-card class="expiration-card">
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
          <v-btn color="success" @click="dialogVisible = false">Got it</v-btn>
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
    this.checkExpiringItems();
  },
  watch: {
    '$store.state.isLoggedIn': function(newVal) {
      if (newVal) {
        console.log('User logged in, checking expiring items...');
        this.checkExpiringItems();
      }
    },
    '$store.getters.foodItems': function(newFoodItems) {
      this.checkExpiringItems(newFoodItems);
    }
  },
  methods: {
    navigateTo(page) {
      this.$router.push({ name: page });
    },
    checkExpiringItems(newFoodItems) {
      const foodItems = newFoodItems || this.$store.getters.foodItems || [];
      const now = new Date();
      const expiringItems = [];

      foodItems.forEach(item => {
        const expiryDate = new Date(item.expirationDate);
        const diffInDays = (expiryDate - now) / (1000 * 60 * 60 * 24);

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

      this.expiringItems = expiringItems;
      this.dialogVisible = expiringItems.length > 0;
      console.log('Expiring items:', expiringItems);
      console.log('Dialog visible:', this.dialogVisible);
    },
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.checkExpiringItems();
      vm.dialogVisible = vm.expiringItems.length > 0; // Ensure the dialog is shown
    });
  },
};
</script>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 80px;
  background-color: #fafafa;
}

.welcome-row {
  padding: 40px 20px;
}

.welcome-title {
  font-size: 2.4em;
  font-weight: bold;
  color: #2E7D32;
  margin-bottom: 20px;
}

.subheading {
  font-size: 1.6em;
  color: #388E3C;
  font-style: italic;
  margin-bottom: 30px;
}

.cta-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 20px;
}

.cta-btn {
  border-radius: 25px;
  font-weight: bold;
  padding: 12px 30px;
  text-transform: none;
}

.welcome-image {
  max-width: 700px;
  height: auto;
  margin: 30px auto;
}

.v-dialog .v-card {
  border-radius: 10px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.expiration-card .v-card-title {
  font-size: 1.8em;
  font-weight: 600;
  color: #2E7D32;
}

.expiration-card .v-list-item-title {
  font-weight: 500;
}

.expiration-card .v-list-item-subtitle {
  font-size: 0.9em;
  color: #d32f2f;
}

.v-btn {
  font-weight: bold;
  text-transform: uppercase;
}
</style>
