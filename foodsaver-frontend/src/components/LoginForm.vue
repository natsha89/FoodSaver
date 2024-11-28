<template>
  <v-container>
    <v-card>
      <v-card-title>Log In</v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid" @keydown.enter="login">
          <v-text-field
              v-model="email"
              :rules="emailRules"
              label="Email"
              required
          ></v-text-field>
          <v-text-field
              v-model="password"
              :rules="passwordRules"
              label="Password"
              type="password"
              required
          ></v-text-field>
        </v-form>
        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="login" :loading="loading" :disabled="loading">Login</v-btn>
        <router-link to="/signup">
          <v-btn text>Sign Up</v-btn>
        </router-link>
      </v-card-actions>
    </v-card>
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

          // Store the user information and token in Vuex and localStorage
          this.$store.commit('setUser', response.data.user); // Assuming your API returns user data
          this.$store.commit('setAuthToken', response.data.token);

          const expiration = new Date().getTime() + 60 * 60 * 1000; // 1 hour expiration
          localStorage.setItem('authToken', response.data.token);  // Persist the token
          localStorage.setItem('tokenExpiration', expiration);
          localStorage.setItem('user', JSON.stringify(response.data.user)); // Persist the user info

          // Redirect to the home page
          this.$router.push('/welcome');  // Redirect to home after successful login
        } catch (error) {
          this.responseMessage = error.response?.data.message || 'An unknown error occurred.';
        } finally {
          this.loading = false;
        }
      }
    },
    checkTokenExpiration() {
      const tokenExpiration = localStorage.getItem('tokenExpiration');
      if (tokenExpiration && new Date().getTime() > tokenExpiration) {
        this.$store.dispatch('logout'); // Logout if token has expired
        this.$router.push('/login'); // Redirect to login
      }
    },
  },
  created() {
    this.checkTokenExpiration(); // Check token expiration on component load
  },
};
</script>

<style scoped>
/* Lägg till eventuella egna stilar här om behövs */
</style>