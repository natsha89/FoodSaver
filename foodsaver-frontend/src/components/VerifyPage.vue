<template>
  <v-container>
    <v-card>
      <v-card-title>Email Verification</v-card-title>
      <v-card-text>
        <div v-if="status === 'success'" class="message success">
          <p>🎉 Congratulations, you are verified! 🎉</p>
          <p>Now log in to your account.</p>
          <v-btn @click="goToLogin" color="primary">Log In</v-btn>
        </div>
        <!-- Expired Status -->
        <div v-if="status === 'expired'" class="message error">
          <p>Uh-oh! The verification link has expired!</p>
          <v-btn @click="resendVerificationLink" color="primary">Resend Verification Code</v-btn>
        </div>

        <!-- Resend Status -->
        <div v-if="status === 'resend'" class="message info">
          <p>📩 A new verification link has been sent to your email address 📩</p>
        </div>
        <div v-if="status === 'error'" class="message error">
          <p>There was an error during the verification process. Please try again.</p>
        </div>
        <div v-if="status === 'waiting'" class="message info">
          <p>Waiting for your verification...</p>
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>


<script>
import http from '../http'; // Your Axios instance for making requests

export default {
  name: 'EmailVerification',
  data() {
    return {
      status: '', // Holds the status of verification (success, expired, etc.)
    };
  },
  created() {
    // Get the status from the URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    this.status = urlParams.get('status') || 'waiting'; // Default to 'waiting' if no status is found
  },
  methods: {
    // Handle resend verification link action
    async resendVerificationLink() {
      const email = prompt('Please enter your email address:');
      if (email) {
        try {
          const response = await http.get(`/api/auth/resend-verification?email=${email}`);
          // Check the response and update the status
          if (response.data.status === 'resend') {
            this.status = 'resend';
          } else {
            this.status = 'error';
          }
        } catch (error) {
          this.status = 'error';
          console.error(error);
        }
      }
    }
  }
};
</script>

<style scoped>
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