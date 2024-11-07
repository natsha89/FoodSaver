<template>
  <v-container>
    <v-card>
      <v-card-title>Log In</v-card-title>
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
      this.responseMessage = ''; // Rensa tidigare meddelanden
      if (this.$refs.form.validate()) {
        this.loading = true; // Aktivera loading-status
        try {
          // Skicka POST-begäran till backend för inloggning
          const response = await this.$http.post('/api/auth/login', {
            email: this.email,
            password: this.password
          });

          // Spara token i localStorage eller sessionStorage
          localStorage.setItem('authToken', response.data.token);

          // Navigera till annan vy (exempelvis hem)
          this.$router.push('/home');
          this.responseMessage = 'Login successful!';
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
/* Add custom styles if needed */
</style>
