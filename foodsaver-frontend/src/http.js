import axios from 'axios';

// Skapa en Axios-instans med bas-URL till backend-servern
const http = axios.create({
    baseURL: 'http://localhost:8081',
    headers: {
        'Content-Type': 'application/json', // Vi skickar data i JSON-format
    },
    timeout: 5000, // Timeout för API-anrop (5 sekunder)
});

// Exportera Axios-instansen så att den kan användas i andra delar av applikationen
export default http;
