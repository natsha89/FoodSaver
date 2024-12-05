<template>
  <v-container class="login-container" fluid>
    <v-row justify="center" align="center" class="login-row">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="login-card" elevation="4">
          <v-card-title class="text-center">Log In</v-card-title>
          <v-card-text>
            <v-form ref="form" v-model="valid" @keydown.enter="login">
              <v-text-field
                  v-model="email"
                  :rules="emailRules"
                  label="Email"
                  required
                  outlined
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  :rules="passwordRules"
                  label="Password"
                  type="password"
                  required
                  outlined
              ></v-text-field>
            </v-form>
            <v-alert v-if="responseMessage" type="error" dismissible class="mt-3">
              {{ responseMessage }}
            </v-alert>
          </v-card-text>
          <v-card-actions class="text-center flex-column"> <!-- Added flex-column here -->
            <!-- Login Button -->
            <v-btn color="primary" @click="login" :loading="loading" :disabled="loading" large class="login-btn">Login</v-btn>
            <!-- Sign Up Button placed below the Login button -->
            <router-link to="/signup">
              <v-btn text class="sign-up-btn">Sign Up</v-btn>
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
  border-radius: 10px;
  background-color: white;
  padding: 20px;
}

.v-text-field {
  margin-bottom: 16px; /* Minska avståndet mellan textfält */
}

.login-btn {
  width: 100%; /* Gör knappen bredare */
  font-weight: bold;
  margin-bottom: 16px; /* Lägg till lite avstånd mellan knapparna */
}

.sign-up-btn {
  width: 100%;
  color: #4CAF50; /* Grön färg för knappen */
  font-weight: bold;
}

.v-alert {
  margin-top: 16px;
}

.v-card-actions {
  display: flex;
  flex-direction: column; /* Vertikal ordning */
  align-items: center; /* Center justering av knapparna */
}
</style>
