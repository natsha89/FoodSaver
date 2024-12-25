/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

// src/plugins/vuetify.js
import 'vuetify/styles'; // Import Vuetify styles
import { createVuetify } from 'vuetify';
import 'vuetify/styles'; // Import Vuetify styles
import * as components from 'vuetify/components'; // Import Vuetify components
import * as directives from 'vuetify/directives'; // Import Vuetify directives

// Create Vuetify instance with custom configurations
const vuetify = createVuetify({
    components,
    directives,
    theme: {
        themes: {
            light: {
                // Custom light theme colors
                primary: '#4CAF50', // Green color for primary actions
                secondary: '#FFFFFF', // White for secondary actions
                accent: '#4CAF50', // Green for accents
                background: '#FFFFFF', // Light background
                surface: '#FFFFFF', // White surface for cards
                error: '#FF5252', // Red for error messages
                info: '#2196F3', // Blue for informational messages
                success: '#4CAF50', // Green for success messages
                warning: '#FFC107', // Yellow for warnings
            },
            dark: {
                // Custom dark theme colors
                primary: '#4CAF50',
                secondary: '#FFFFFF',
                accent: '#4CAF50',
                background: '#121212', // Dark background
                surface: '#1E1E1E', // Dark surface for cards
                error: '#FF5252',
                info: '#2196F3',
                success: '#4CAF50',
                warning: '#FFC107',
            },
        },
    },
    icons: {
        // Optional: Import specific icon sets if needed
        iconfont: 'mdi', // Use Material Design Icons
        values: {
            // Add custom icons here
            menu: 'mdi-menu',
            // You can add more custom icons as needed
        },
    },
});

export default vuetify;