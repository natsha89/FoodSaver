<template>
  <v-container class="user-notifications">
    <v-row justify="center">
      <v-col cols="12" md="10">
        <h2 class="page-title">User Notifications</h2>

        <v-progress-circular v-if="loading" indeterminate color="primary" class="mt-4"></v-progress-circular>

        <v-alert v-if="error" type="error" class="mt-4">
          {{ error }}
        </v-alert>

        <v-card v-else-if="notifications.length" class="notifications-card mt-4">
          <v-card-title class="list-title">Your Notifications</v-card-title>
          <v-divider></v-divider>
          <v-list dense>
            <v-list-item v-for="notification in notifications" :key="notification.id">
              <v-row>
                <v-col cols="9">
                  <span class="notification-message">{{ notification.message }}</span>
                  <br>
                  <small class="notification-timestamp">
                    {{ formatTimestamp(notification.timestamp) }}
                  </small>
                </v-col>
                <v-col cols="3" class="text-right">
                  <v-btn
                      @click="markAsRead(notification.id)"
                      small
                      color="primary"
                      outlined
                      :disabled="notification.isRead"
                  >
                    {{ notification.isRead ? 'Read' : 'Mark Read' }}
                  </v-btn>
                </v-col>
              </v-row>
            </v-list-item>
          </v-list>
        </v-card>

        <v-alert v-else type="info" class="mt-4">
          No notifications found.
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      error: null,
      notifications: [],
    };
  },
  computed: {
    unreadNotifications() {
      return this.notifications.filter(n => !n.isRead);
    },
  },
  created() {
    this.fetchNotifications();
  },
  methods: {
    formatTimestamp(timestamp) {
      return new Date(timestamp).toLocaleString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    async fetchNotifications() {
      try {
        this.loading = true;
        this.error = null;
        const response = await this.$store.dispatch('fetchNotifications');
        this.notifications = response.notifications || [];
      } catch (error) {
        this.error = 'Failed to load notifications. Please try again.';
        this.$toast.error('Error fetching notifications');
      } finally {
        this.loading = false;
      }
    },
    async markAsRead(notificationId) {
      try {
        await this.$store.dispatch('markNotificationAsRead', notificationId);
        const notification = this.notifications.find(n => n.id === notificationId);
        if (notification) {
          notification.isRead = true;
        }
        this.$toast.success('Notification marked as read');
      } catch (error) {
        this.$toast.error('Failed to mark notification as read');
      }
    },
  },
};
</script>

<style scoped>
.user-notifications {
  margin-top: 40px;
}

.page-title {
  font-size: 2em;
  color: #1976D2;
  text-align: center;
  margin-bottom: 20px;
}

.notifications-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.list-title {
  font-size: 1.3em;
  color: #1976D2;
}

.notification-message {
  font-weight: 500;
}

.notification-timestamp {
  color: #757575;
}
</style>