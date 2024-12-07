<template>
  <v-container class="user-notifications">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <h2 class="page-title">My Notifications</h2>

        <v-img
            src="/alert.png"
            alt="Alert Image"
            class="alert-image"
            aspect-ratio="2/1"
            contain
            @error="handleImageError"
        ></v-img>

        <v-progress-circular
            v-if="loading"
            indeterminate
            color="primary"
            class="mt-4"
        ></v-progress-circular>

        <v-card
            v-else-if="notifications.length"
            class="notifications-list-card mt-4"
        >
          <v-card-title class="list-title">
            Your Notifications
            <v-spacer></v-spacer>
            <v-btn
                @click="markAllAsRead"
                color="primary"
                text
                :disabled="!notifications.some(n => !n.read)"
            >
              Mark All as Read
            </v-btn>
          </v-card-title>
          <v-divider></v-divider>

          <v-list dense>
            <v-list-item
                v-for="notification in notifications"
                :key="notification.id"
                :class="{ 'unread-notification': !notification.read }"
            >
              <v-row>
                <v-col cols="9">
                  <span class="notification-title">
                    {{ notification.title }}
                  </span>
                  <p class="notification-message">
                    {{ notification.message }}
                  </p>
                  <small class="notification-date">
                    {{ formatDate(notification.createdAt) }}
                  </small>
                </v-col>
                <v-col cols="3" class="text-right">
                  <v-btn
                      v-if="!notification.read"
                      @click="markNotificationAsRead(notification.id)"
                      small
                      color="primary"
                      outlined
                  >
                    Mark Read
                  </v-btn>
                </v-col>
              </v-row>
            </v-list-item>
          </v-list>
        </v-card>

        <v-alert
            v-else
            type="info"
            class="mt-4"
        >
          No notifications found.
        </v-alert>

        <v-alert
            v-if="error"
            type="error"
            class="mt-4"
        >
          {{ error }}
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
      notifications: [],
      error: null,
    };
  },
  created() {
    this.loadNotifications();
  },
  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleString() : 'N/A';
    },
    handleImageError(event) {
      console.error('Image load error:', event);
      this.error = 'Failed to load alert image';
    },
    async loadNotifications() {
      try {
        this.loading = true;
        this.error = null;

        // Add more detailed error logging
        const response = await this.$store.dispatch('fetchNotifications');

        console.log('Full response:', response);

        if (!response || !response.notifications) {
          throw new Error('Invalid response structure');
        }

        this.notifications = response.notifications;
      } catch (error) {
        console.error('Notification load error:', error);
        this.error = error.message || 'Failed to load notifications';
        this.$toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },
    async markNotificationAsRead(notificationId) {
      try {
        this.error = null;
        await this.$store.dispatch('markNotificationAsRead', notificationId);

        const index = this.notifications.findIndex(n => n.id === notificationId);
        if (index !== -1) {
          this.notifications[index].read = true;
        }

        this.$toast.success('Notification marked as read');
      } catch (error) {
        console.error('Mark notification read error:', error);
        this.error = 'Failed to mark notification as read';
        this.$toast.error(this.error);
      }
    },
    async markAllAsRead() {
      try {
        this.error = null;
        await this.$store.dispatch('markAllNotificationsAsRead');

        this.notifications = this.notifications.map(n => ({...n, read: true}));

        this.$toast.success('All notifications marked as read');
      } catch (error) {
        console.error('Mark all notifications read error:', error);
        this.error = 'Failed to mark all notifications as read';
        this.$toast.error(this.error);
      }
    },
  },
};
</script>

<style scoped>
.alert-image {
  border-radius: 8px;
  margin-top: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-height: 300px;
  object-fit: cover;
}</style>