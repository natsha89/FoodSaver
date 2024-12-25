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
  <v-container class="verification-container">
    <v-card class="verification-card">
      <v-card-title class="title">Verification Success</v-card-title>
      <v-card-text>
        <div class="message success">
          <p>🎉 Thank you! Your registration is now complete. Please log in to start using your Foodsaver. 🎉</p>
          <v-btn @click="goToLogin" color="primary">Log In</v-btn>
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'VerificationSuccess',
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
.verification-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh; /* Full viewport height */
}

.verification-card {
  text-align: center;
}

.title {
  text-align: center;
}

.message {
  padding: 15px;
  margin: 20px 0;
  border-radius: 5px;
  font-size: 1.2em;
}
.success {
  background-color: #d4edda;
  color: #155724;
}
.error {
  background-color: #f8d7da;
  color: #721c24;
}
.info {
  background-color: #cce5ff;
  color: #004085;
}
button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}
button:hover {
  background-color: #0056b3;
}
</style>
