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
  <div v-if="alertMessage" class="alert" v-bind:style="alertStyle">
    <p>{{ alertMessage }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      alertMessage: '', // Håller notifieringens meddelande
      alertStyle: {
        position: 'fixed',
        top: '20px',
        right: '20px',
        backgroundColor: '#ffcc00',
        color: '#000',
        padding: '10px 20px',
        borderRadius: '5px',
        fontSize: '16px',
        boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
        zIndex: 1000,
        opacity: 0.9,
        animation: 'fadeInOut 5s forwards',
      },
    };
  },
  methods: {
    // Anropar servern för att hämta notifieringar
    async checkExpirationNotifications() {
      try {
        const response = await axios.get('/api/foodItems'); // Anpassa efter din server
        const foodItems = response.data;

        // Gå igenom alla matvaror och kolla om det finns ett alertMessage
        for (const foodItem of foodItems) {
          if (foodItem.alertMessage) {
            this.alertMessage = foodItem.alertMessage; // Sätt notifieringen
            break; // Visar endast en notifiering
          }
        }
      } catch (error) {
        console.error('Error fetching food items:', error);
      }
    },
  },
  mounted() {
    // Kolla notifieringar när komponenten laddas
    this.checkExpirationNotifications();

    // Sätt en interval för att kontrollera notifieringar varje dag
    setInterval(this.checkExpirationNotifications, 86400000); // 86400000 ms = 1 dag
  },
};
</script>

<style scoped>
@keyframes fadeInOut {
  0% { opacity: 0; }
  20% { opacity: 1; }
  80% { opacity: 1; }
  100% { opacity: 0; }
}

.alert {
  animation: fadeInOut 5s forwards;
}
</style>
