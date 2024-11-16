<template>
  <v-app>
    <v-container class="profile-container" fluid>
      <v-card outlined class="profile-card">
        <v-card-title>
          <div class="header">
            <h2>My Account</h2>
            <p class="subtitle">Manage your profile and settings</p>
          </div>
        </v-card-title>
        <v-card-text>
          <v-row>
            <!-- Profile Details Section -->
            <v-col cols="12" md="6">
              <v-card flat class="details-card">
                <v-card-title>Profile Details</v-card-title>
                <v-list dense>
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>
                        <strong>Name:</strong> {{ user?.name || 'N/A' }}
                      </v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>
                        <strong>Email:</strong> {{ user?.email || 'N/A' }}
                      </v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>
                        <strong>Status:</strong>
                        <span v-if="user?.emailVerified" class="verified">Verified</span>
                        <span v-else class="unverified">Not Verified</span>
                      </v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                <v-btn
                    v-if="!user?.emailVerified"
                    color="green"
                    class="resend-btn"
                    @click="resendVerificationEmail"
                >
                  Resend Verification Email
                </v-btn>
              </v-card>
            </v-col>

            <!-- Actions Section -->
            <v-col cols="12" md="6">
              <v-card flat class="actions-card">
                <v-card-title>Actions</v-card-title>
                <v-list dense>
                  <v-list-item>
                    <v-btn color="primary" block @click="editProfile">
                      Edit Profile
                    </v-btn>
                  </v-list-item>
                  <v-list-item>
                    <v-btn color="error" block @click="deleteAccount">
                      Delete Account
                    </v-btn>
                  </v-list-item>
                  <v-list-item>
                    <v-btn color="secondary" block @click="logout">
                      Logout
                    </v-btn>
                  </v-list-item>
                </v-list>
              </v-card>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </v-app>
</template>

<script>
import http from '../http'; // Axios instance for API requests
import {mapActions, mapGetters} from 'vuex';

export default {
  name: 'userProfile',
  data() {
    return {
      loading: false,
    };
  },
  computed: {
    ...mapGetters(['user']), // Access user data from Vuex
  },
  methods: {
    ...mapActions(['fetchUser', 'logout']),
    async resendVerificationEmail() {
      try {
        this.loading = true;
        await http.post('/api/auth/resend-verification-email', {email: this.user.email});
        alert('Verification email sent successfully.');
      } catch (error) {
        console.error('Error resending verification email:', error);
        alert('Failed to resend verification email.');
      } finally {
        this.loading = false;
      }
    },
    async deleteAccount() {
      if (confirm('Are you sure you want to delete your account? This action is irreversible.')) {
        try {
          await http.delete(`/api/users/${this.user.id}`);
          alert('Account deleted successfully.');
          this.logout(); // Log out the user after account deletion
        } catch (error) {
          console.error('Error deleting account:', error);
          alert('Failed to delete account.');
        }
      }
    },
    editProfile() {
      // Navigate to profile editing view
      this.$router.push('/edit-profile');
    },
  },
  async mounted() {
    // Fetch user data on component load
    try {
      await this.fetchUser(); // Fetch user from the API and update Vuex store
    } catch (error) {
      console.error('Error fetching user data:', error);
      alert('Failed to load profile information.');
    }
  },
};
</script>

<style scoped>
.profile-container {
  margin-top: 20px;
}

.profile-card {
  padding: 20px;
}

.header {
  text-align: center;
}

.subtitle {
  font-size: 0.9em;
  color: gray;
}

.details-card,
.actions-card {
  padding: 10px;
}

.verified {
  color: #4caf50;
  font-weight: bold;
}

.unverified {
  color: #f44336;
  font-weight: bold;
}

.resend-btn {
  margin-top: 10px;
}
</style>
