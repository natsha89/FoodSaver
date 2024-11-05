<template>
  <v-container>
    <v-card>
      <v-card-title>Sign In</v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">
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
export default {
  name: 'LoginForm',
  data() {
    return {
      valid: false,
      email: '',
      password: '',
      loading: false, // State to indicate loading
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 6 || 'Password must be at least 6 characters',
      ],
      responseMessage: '', // Variable to store response messages
    };
  },
  methods: {
    async login() {
      this.responseMessage = ''; // Reset the message
      if (this.$refs.form.validate()) {
        this.loading = true; // Set loading state
        try {
          const response = await this.$http.post('/api/auth/login', null, {
            params: { email: this.email, password: this.password },
          });
          this.responseMessage = response.data.message; // Display welcome message
          this.$router.push('/home'); // Redirect to home or another page
        } catch (error) {
          if (error.response && error.response.data.message) {
            this.responseMessage = error.response.data.message; // Display error message from backend
          } else {
            this.responseMessage = 'An unknown error occurred.'; // Generic error message
          }
        } finally {
          this.loading = false; // Reset loading state
        }
      }
    },
  },
};
</script>
