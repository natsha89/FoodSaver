<template>
  <v-container class="user-profile">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <!-- Tidigare kod är densamma -->

        <!-- Edit Dialog med nya tillägg -->
        <v-dialog v-model="editDialog" max-width="500px">
          <v-card v-if="selectedUser">
            <!-- Tidigare innehåll -->

            <v-card-subtitle class="pa-0 mt-4">Dietary Preferences</v-card-subtitle>
            <v-chip-group column>
              <v-chip
                  v-for="(preference, index) in selectedUser.dietaryPreferences"
                  :key="index"
                  small
                  close
                  @click:close="selectedUser.dietaryPreferences.splice(index, 1)"
              >
                {{ preference }}
              </v-chip>
              <v-chip small outlined @click="openDietaryPreferenceDialog">
                + Add
              </v-chip>
            </v-chip-group>

            <v-card-subtitle class="pa-0 mt-4">Allergies</v-card-subtitle>
            <v-chip-group column>
              <v-chip
                  v-for="(allergy, index) in selectedUser.allergies"
                  :key="index"
                  small
                  close
                  color="red"
                  text-color="white"
                  @click:close="selectedUser.allergies.splice(index, 1)"
              >
                {{ allergy }}
              </v-chip>
              <v-chip small outlined color="red" text-color="red" @click="openAllergyDialog">
                + Add
              </v-chip>
            </v-chip-group>
          </v-card>
        </v-dialog>

        <!-- Dietary Preferences Dialog -->
        <v-dialog v-model="dietaryPreferenceDialog" max-width="300px">
          <v-card>
            <v-card-title>Select Dietary Preference</v-card-title>
            <v-list>
              <v-list-item
                  v-for="preference in filteredDietaryPreferences"
                  :key="preference"
                  @click="addSelectedDietaryPreference(preference)"
              >
                <v-list-item-title>{{ preference }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card>
        </v-dialog>

        <!-- Allergies Dialog -->
        <v-dialog v-model="allergyDialog" max-width="300px">
          <v-card>
            <v-card-title>Select Allergy</v-card-title>
            <v-list>
              <v-list-item
                  v-for="allergy in filteredAllergies"
                  :key="allergy"
                  @click="addSelectedAllergy(allergy)"
              >
                <v-list-item-title>{{ allergy }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card>
        </v-dialog>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    return {
      loading: true,
      error: null,
      editDialog: false,
      selectedUser: null,
      dietaryPreferenceDialog: false,
      allergyDialog: false,
      availableDietaryPreferences: [
        'Vegetarian',
        'Vegan',
        'Gluten-free',
        'Lactose-free',
        'Keto',
        'Paleo',
        'Halal',
        'Kosher'
      ],
      availableAllergies: [
        'Nuts',
        'Milk',
        'Eggs',
        'Fish',
        'Shellfish',
        'Soy',
        'Wheat',
        'Peanuts'
      ]
    };
  },
  computed: {
    filteredDietaryPreferences() {
      return this.availableDietaryPreferences.filter(
          pref => !this.selectedUser.dietaryPreferences.includes(pref)
      );
    },
    filteredAllergies() {
      return this.availableAllergies.filter(
          allergy => !this.selectedUser.allergies.includes(allergy)
      );
    }
  },
  methods: {
    // ... tidigare metoder ...

    openDietaryPreferenceDialog() {
      this.dietaryPreferenceDialog = true;
    },

    openAllergyDialog() {
      this.allergyDialog = true;
    },

    addSelectedDietaryPreference(preference) {
      this.selectedUser.dietaryPreferences.push(preference);
      this.dietaryPreferenceDialog = false;
    },

    addSelectedAllergy(allergy) {
      this.selectedUser.allergies.push(allergy);
      this.allergyDialog = false;
    }
  }
};
</script>

<style scoped>
.profile-card {
  margin-top: 20px;
}

.page-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>
