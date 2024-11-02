// src/http.js

import axios from 'axios';

// Create an instance of axios with default configurations
const http = axios.create({
    baseURL: 'http://localhost:8080/api', // Adjust this URL to match your backend
    timeout: 1000, // Timeout in milliseconds
});

// You can also set up interceptors here if needed
http.interceptors.response.use(
    response => response,
    error => {
        // Handle errors globally if required
        return Promise.reject(error);
    }
);

export default http; // Export the instance
