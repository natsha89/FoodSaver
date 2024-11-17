import axios from 'axios';

// Dynamiskt hämta token
const getAuthToken = () => localStorage.getItem('token') || '';

// Skapa Axios-instans
const http = axios.create({
    baseURL: 'http://localhost:8081',
    headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${getAuthToken()}`,
    },
    timeout: 5000,
});

// Interceptor för att dynamiskt lägga till token
http.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Interceptor för att hantera API-responsfel
http.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            console.error('API Error:', error.response.data);
        } else if (error.request) {
            console.error('No response received:', error.message);
        } else {
            console.error('Request setup error:', error.message);
        }
        return Promise.reject(error);
    }
);

export default http;
