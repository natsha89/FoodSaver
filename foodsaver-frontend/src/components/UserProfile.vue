<template>
  <div v-if="user" class="user-profile">
    <h2>{{ user.fullName }}</h2>
    <p>Email: {{ user.email }}</p>
  </div>
  <div v-else>
    <v-progress-circular indeterminate></v-progress-circular>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false, // Lägg till loading för att hantera laddningsstatus
    };
  },
  computed: {
    user() {
      return this.$store.getters.user; // Hämta användardata från Vuex
    },
  },
  created() {
    this.loadUser(); // Ladda användardata vid skapande
  },
  methods: {
    async loadUser() {
      try {
        this.loading = true; // Visa laddningsindikator
        await this.$store.dispatch('fetchUser'); // Hämta användardata från API
      } catch (error) {
        console.error('Failed to load user:', error);
        this.$toast.error('Failed to load user'); // Visa felmeddelande
      } finally {
        this.loading = false; // Dölj laddningsindikator
      }
    },
  },
};
</script>
