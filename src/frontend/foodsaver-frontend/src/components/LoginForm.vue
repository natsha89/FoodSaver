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
  <v-container class="login-container" fluid>
    <v-row justify="center" align="center" class="login-row">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="login-card" elevation="8">
          <!-- New text at the top -->
          <v-card-title class="text-center welcome-text">
            👩🏻‍🍳👨🏻‍🍳 Login to your FoodSaver world 👩🏻‍🍳👨🏻‍🍳
          </v-card-title>

          <v-card-text>
            <v-form ref="form" v-model="valid" @keydown.enter="login">
              <v-text-field
                  v-model="email"
                  :rules="emailRules"
                  label="Email"
                  required
                  outlined
                  dense
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  :rules="passwordRules"
                  label="Password"
                  type="password"
                  required
                  outlined
                  dense
              ></v-text-field>
            </v-form>

            <!-- Error message styling -->
            <v-alert v-if="responseMessage" type="error" dismissible class="mt-3 error-alert">
              {{ responseMessage }}
            </v-alert>
          </v-card-text>

          <!-- Buttons section -->
          <v-card-actions class="text-center flex-column">
            <v-btn color="primary" @click="login" :loading="loading" :disabled="loading" large class="login-btn">Login</v-btn>
            <router-link to="/signup">
              <v-btn text class="sign-up-btn">Don't have an account? Sign Up</v-btn>
            </router-link>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// Importera http.js (Axios-instansen)
import http from '../http';

export default {
  name: 'LoginForm',
  data() {
    return {
      valid: false,
      email: '',
      password: '',
      loading: false, // Loading state
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 6 || 'Password must be at least 6 characters',
      ],
      responseMessage: '', // Store response message
    };
  },
  methods: {
    async login() {
      this.responseMessage = '';
      if (this.$refs.form.validate()) {
        this.loading = true;
        try {
          const response = await http.post('/api/auth/login', {
            email: this.email,
            password: this.password
          });

          console.log('Token after login:', response.data.data.token);

          // Store the user information and token in Vuex and localStorage
          this.$store.commit('setUser', response.data.data.user);
          this.$store.commit('setAuthToken', response.data.data.token);
          localStorage.setItem('authToken', response.data.data.token);
          localStorage.setItem('user', JSON.stringify(response.data.data.user));

          this.$router.push('/welcome');
        } catch (error) {
          this.responseMessage = error.response?.data.message || 'An unknown error occurred.';
        } finally {
          this.loading = false;
        }
      }
    }
  },
};
</script>

<style scoped>
/* Styling för hela login-formuläret */
.login-container {
  margin-top: 50px;
  background-color: #f9f9f9;
}

.login-row {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh; /* Full höjd */
}

.login-card {
  border-radius: 12px;
  background-color: white;
  padding: 30px;
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
}

.welcome-text {
  font-size: 1em;
  color: #388E3C;
  font-weight: bold;
  margin-bottom: 30px;
}

.v-text-field {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%; /* Gör knappen bredare */
  font-weight: bold;
  margin-bottom: 16px; /* Lägg till lite avstånd mellan knapparna */
  transition: background-color 0.3s ease;
}

.login-btn:hover {
  background-color: #2C6A32;
}

.sign-up-btn {
  width: 100%;
  color: #4CAF50; /* Grön färg för knappen */
  font-weight: bold;
  transition: color 0.3s ease;
}

.sign-up-btn:hover {
  color: #388E3C;
}

.v-alert {
  margin-top: 16px;
}

.v-alert .v-alert__wrapper {
  font-size: 1em;
}

.v-card-actions {
  display: flex;
  flex-direction: column; /* Vertikal ordning */
  align-items: center; /* Center justering av knapparna */
}

.error-alert {
  background-color: #FFCDD2; /* Light red for error messages */
  color: #D32F2F; /* Dark red for text */
  font-weight: bold;
  padding: 10px;
  border-radius: 8px;
}
</style>
