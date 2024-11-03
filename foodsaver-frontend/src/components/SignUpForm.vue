<template>
  <v-container>
    <v-card>
      <v-card-title>Sign Up</v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">
          <v-text-field
              v-model="fullName"
              :rules="nameRules"
              label="Full Name"
              required
          ></v-text-field>
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
          <v-text-field
              v-model="allergies"
              label="Allergies (comma separated)"
              placeholder="e.g. Gluten, Nuts"
          ></v-text-field>
        </v-form>
        <v-alert v-if="responseMessage" type="error" dismissible>
          {{ responseMessage }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="signUp" :disabled="!valid">Create Account</v-btn>
        <router-link to="/login">
          <v-btn text>Sign In</v-btn>
        </router-link>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'SignUpForm',
  data() {
    return {
      valid: false,
      fullName: '',
      email: '',
      password: '',
      allergies: '',
      nameRules: [
        v => !!v || 'Full Name is required',
      ],
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
    async signUp() {
      this.responseMessage = ''; // Reset the message
      if (this.$refs.form.validate()) {
        try {
          const response = await this.$http.post('/api/auth/register', {
            fullName: this.fullName,
            email: this.email,
            password: this.password,
            allergies: this.allergies.split(',').map(a => a.trim()), // Process allergies input
          });
          this.responseMessage = response.data.message; // Display success message
          // Optionally, redirect the user to the login page or another page here
        } catch (error) {
          if (error.response) {
            this.responseMessage = error.response.data.message || 'An unknown error occurred.';
          } else {
            this.responseMessage = 'Network error: Unable to reach the server.';
          }
        }
      }
    },
  },
};
</script>

<style scoped>
/* Add any styles you want here */
</style>
