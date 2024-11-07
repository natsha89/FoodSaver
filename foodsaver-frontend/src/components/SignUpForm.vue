<template>
  <v-container>
    <v-card>
      <v-card-title>Create Account</v-card-title>
      <v-card-text>
        <!-- Sign up form -->
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
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      valid: false, // Form validation state
      isSubmitting: false, // Submit button state (loading indicator)
      fullName: '',
      email: '',
      password: '',
      selectedAllergies: [], // Selected allergies array
      selectedDietaryPreferences: [], // Selected dietary preferences array
      allergyOptions: ['Gluten', 'Nuts', 'Dairy', 'Soy', 'Eggs', 'None'], // Allergy options
      dietaryOptions: ['Vegan', 'Vegetarian', 'Keto', 'Paleo', 'None'], // Dietary options
      nameRules: [v => !!v || 'Full Name is required'], // Full Name validation
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid'
      ], // Email validation
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 6 || 'Password must be at least 6 characters'
      ], // Password validation
      responseMessage: '', // Variable to store response messages (error/success)
      responseType: 'error', // Type of response message (default: error)
    };
  },
  methods: {
    // Sign up function
    async signUp() {
      this.responseMessage = ''; // Reset the response message
      this.responseType = 'error'; // Default response type is error
      if (this.$refs.form.validate()) { // Validate form
        this.isSubmitting = true; // Set submitting state to true
        try {
          const response = await this.$http.post('/api/auth/register', {
            fullName: this.fullName,
            email: this.email,
            password: this.password,
            allergies: this.selectedAllergies, // Send selected allergies
            dietaryPreferences: this.selectedDietaryPreferences // Send selected dietary preferences
          });
          this.responseMessage = response.data.message; // Display success message
          this.responseType = 'success'; // Set response type to success
          this.$router.push('/login'); // Redirect to login page after successful registration
        } catch (error) {
          // Handle error response gracefully
          this.responseMessage = error.response?.data.message || 'An unknown error occurred.';
        } finally {
          this.isSubmitting = false; // Set submitting state to false
        }
      }
    },
  },
};
</script>

<style scoped>
/* Optional styling */
.v-form {
  max-width: 500px;
  margin: auto;
}
</style>
