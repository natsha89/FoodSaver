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
  <v-container>
    <v-card>
      <v-card-title>Create Account</v-card-title>
      <v-card-text>
        <!-- Signup form -->
        <v-form ref="form" v-model="valid" @keydown.enter="signUp">
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

          <!-- Allergies selection -->
          <v-autocomplete
              v-model="selectedAllergies"
              :items="allergyOptions"
              label="Select Allergies"
              multiple
              chips
              clearable
              hint="Select any allergies you have"
          ></v-autocomplete>

          <!-- Dietary Preferences selection -->
          <v-autocomplete
              v-model="selectedDietaryPreferences"
              :items="dietaryOptions"
              label="Select Dietary Preferences"
              multiple
              chips
              clearable
              hint="Select any dietary preferences"
          ></v-autocomplete>
        </v-form>

        <!-- Display response message if any -->
        <v-alert v-if="responseMessage" :type="responseType" dismissible>
          {{ responseMessage }}
        </v-alert>
      </v-card-text>

      <v-card-actions>
        <!-- Create account button -->
        <v-btn @click="signUp" :disabled="!valid || isSubmitting">
          <span v-if="isSubmitting">Creating...</span>
          <span v-else>Create Account</span>
        </v-btn>

        <!-- Sign In link -->
        <router-link to="/login">
          <v-btn text>Sign In</v-btn>
        </router-link>
      </v-card-actions>
    </v-card>

    <!-- Success dialog -->
    <v-dialog v-model="dialogVisible" max-width="500px">
      <v-card>
        <v-card-title class="headline">You are successfully registered ✅</v-card-title>
        <v-card-text>
          <p>📩 A verification link has been sent to your email. Please check your email! 📩</p>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="closeDialog">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import http from '../http';

export default {
  data() {
    return {
      valid: false,
      isSubmitting: false,
      dialogVisible: false,
      fullName: '',
      email: '',
      password: '',
      selectedAllergies: [],
      selectedDietaryPreferences: [],
      allergyOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs', 'None'],
      dietaryOptions: ['Vegan', 'Vegetarian', 'Keto', 'Paleo', 'None'],
      nameRules: [v => !!v || 'Full Name is required'],
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid'
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 6 || 'Password must be at least 6 characters'
      ],
      responseMessage: '',
      responseType: 'error',
    };
  },
  methods: {
    async signUp() {
      this.responseMessage = '';
      this.responseType = 'error';
      if (this.$refs.form.validate()) {
        this.isSubmitting = true;
        try {
          const response = await http.post('/api/auth/register', {
            fullName: this.fullName,
            email: this.email,
            password: this.password,
            allergies: this.selectedAllergies,
            dietaryPreferences: this.selectedDietaryPreferences
          });

          // Hantera lyckad registrering
          this.responseMessage = response.data.message || 'Account created successfully!';
          this.responseType = 'success';

          // Visa dialog istället för omdirigering
          this.dialogVisible = true;
        } catch (error) {
          // Hantera eventuella fel vid registrering
          this.responseMessage = error.response?.data.message || 'An unknown error occurred.';
        } finally {
          this.isSubmitting = false;
        }
      }
    },
    closeDialog() {
      this.dialogVisible = false;
      this.$router.push('/login');
    }
  },
};
</script>

<style scoped>
.v-form {
  max-width: 500px;
  margin: auto;
}
</style>