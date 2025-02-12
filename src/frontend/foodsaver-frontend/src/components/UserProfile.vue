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
  <v-container class="user-profile">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <h2 class="page-title">My Account</h2>

        <v-card v-if="user" class="profile-card">
          <v-card-title class="profile-title">
            Profile Details
          </v-card-title>
          <v-card-text>
            <!-- Profile Details -->
            <v-form>
              <v-text-field
                  v-model="user.fullName"
                  label="Full Name"
                  outlined
                  readonly
                  class="mb-3"
              ></v-text-field>

              <v-text-field
                  v-model="user.email"
                  label="Email Address"
                  outlined
                  readonly
              ></v-text-field>

              <v-card-subtitle class="pa-0 mt-4">Dietary Preferences</v-card-subtitle>
              <v-chip-group column>
                <v-chip
                    v-for="preference in user.dietaryPreferences"
                    :key="preference"
                    small
                    class="chip"
                >
                  {{ preference }}
                </v-chip>
              </v-chip-group>

              <v-card-subtitle class="pa-0 mt-4">Allergies</v-card-subtitle>
              <v-chip-group column>
                <v-chip
                    v-for="allergy in user.allergies"
                    :key="allergy"
                    small
                    color="red"
                    text-color="white"
                    class="chip"
                >
                  {{ allergy }}
                </v-chip>
              </v-chip-group>
            </v-form>

            <!-- New buttons for My Food Items and My Recipes -->
            <v-row justify="center" class="mt-4">
              <v-col cols="12" sm="5">
                <v-btn
                    color="primary"
                    block
                    class="action-btn"
                    @click="navigateTo('/ingredient-list')"
                >
                  My Food Items
                </v-btn>
              </v-col>
              <v-col cols="12" sm="5">
                <v-btn
                    color="primary"
                    block
                    class="action-btn"
                    @click="navigateTo('/saved-recipes')"
                >
                  My Recipes
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>

          <!-- Action Buttons -->
          <v-card-actions>
            <v-btn
                color="primary"
                text
                @click="openEditDialog"
            >
              Edit Profile
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
                color="error"
                text
                @click="confirmDeleteAccount"
            >
              Delete Account
            </v-btn>
          </v-card-actions>
        </v-card>

        <!-- Progress and Alerts -->
        <v-progress-linear
            v-if="loading"
            indeterminate
            color="primary"
        ></v-progress-linear>

        <v-alert
            v-if="error"
            type="error"
            class="mt-4"
            dismissible
        >
          {{ error }}
        </v-alert>
      </v-col>
    </v-row>

    <!-- Edit Profile Dialog -->
    <v-dialog v-model="isEditing" max-width="600px">
      <v-card>
        <v-card-title>Edit Profile</v-card-title>
        <v-card-text>
          <v-form ref="profileForm" @submit.prevent="saveProfile">
            <v-text-field
                v-model="editedUser.fullName"
                label="Full Name"
                outlined
                class="mb-3"
                :rules="[rules.required]"
                :error-messages="fullNameErrors"
                @input="validateFullName"
            ></v-text-field>

            <v-card-subtitle class="pa-0 mt-4">Dietary Preferences</v-card-subtitle>
            <v-select
                v-model="editedUser.dietaryPreferences"
                :items="allDietaryPreferences"
                label="Select Dietary Preferences"
                multiple
                chips
                deletable-chips
            ></v-select>

            <v-card-subtitle class="pa-0 mt-4">Allergies</v-card-subtitle>
            <v-select
                v-model="editedUser.allergies"
                :items="allAllergies"
                label="Select Allergies"
                multiple
                chips
                deletable-chips
                color="red"
            ></v-select>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn color="success" @click="saveProfile" :loading="saving">Save</v-btn>
          <v-btn color="grey" @click="cancelEditing">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
export default {
  name: "UserProfile",
  data() {
    return {
      loading: true,
      error: null,
      isEditing: false,
      editedUser: {},
      saving: false,
      fullNameErrors: [],
      emailErrors: [],
      allDietaryPreferences: ['Vegan', 'Vegetarian', 'Gluten-Free', 'Keto', 'Paleo', 'None'], // Populate with all available dietary preferences
      allAllergies: ['Peanuts', 'Tree Nuts', 'Dairy', 'Gluten', 'Shellfish', 'Soy', 'None'], // Populate with all available allergies
      rules: {
        required: value => !!value || 'This field is required',
        email: value => /.+@.+\..+/.test(value) || 'Please enter a valid email',
      }
    };
  },
  computed: {
    user() {
      return this.$store.getters.user;
    },
  },
  created() {
    this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      try {
        this.loading = true;
        this.error = null;
        await this.$store.dispatch("fetchUser");

        if (this.user) {
          this.$toast.success("Profile loaded successfully");
        }
      } catch (error) {
        this.error =
            error.response?.data?.message || "Failed to load user profile";
        this.$toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },
    openEditDialog() {
      this.editedUser = {
        ...this.user,
        dietaryPreferences: [...(this.user.dietaryPreferences || [])],
        allergies: [...(this.user.allergies || [])],
      };
      this.isEditing = true;
    },
    cancelEditing() {
      this.isEditing = false;
      this.editedUser = {};
    },
    validateFullName() {
      this.fullNameErrors = this.editedUser.fullName ? [] : ['This field is required'];
    },
    async saveProfile() {
      const isValid = await this.$refs.profileForm.validate();

      if (!isValid) {
        this.$toast.error("Please fill out all required fields correctly");
        return;
      }

      this.saving = true;
      try {
        await this.$store.dispatch("updateUser", {
          fullName: this.editedUser.fullName,
          dietaryPreferences: this.editedUser.dietaryPreferences,
          allergies: this.editedUser.allergies,
        });

        this.$toast.success("Profile updated successfully");
        this.isEditing = false;
      } catch (error) {
        this.error =
            error.response?.data?.message || "Failed to update profile";
        this.$toast.error(this.error);
      } finally {
        this.saving = false;
      }
    },
    async confirmDeleteAccount() {
      const confirm = await this.$dialog.confirm({
        text: "Are you sure you want to delete your account? This action cannot be undone.",
        title: "Delete Account",
      });

      if (confirm) {
        try {
          await this.$store.dispatch("deleteUser");
          this.$toast.success("Account deleted successfully");
          this.$router.push("/login");
        } catch (error) {
          this.error =
              error.response?.data?.message || "Failed to delete account";
          this.$toast.error(this.error);
        }
      }
    },
    navigateTo(route) {
      this.$router.push(route);
    },
  },
};
</script>

<style scoped>
.user-profile {
  padding: 20px;
}

.page-title {
  text-align: center;
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
}

.profile-card {
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.v-card-title.profile-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4CAF50;
  border-bottom: 1px solid #E0E0E0;
  padding-bottom: 8px;
  margin-bottom: 12px;
}

.v-text-field {
  margin-bottom: 15px;
}

.v-card-subtitle {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
}

.v-chip {
  margin-bottom: 5px;
  border: 1px solid #ddd;
  font-size: 0.85rem;
}

.v-chip-group {
  margin-top: 8px;
}

.action-btn {
  font-weight: 600;
  font-size: 1rem;
}

.v-row {
  gap: 10px;
}

.add-chip {
  cursor: pointer;
  text-align: center;
}

.chip {
  margin-bottom: 10px;
}
</style>
