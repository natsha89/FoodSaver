import axios from 'axios';

// Skapa en instans av Axios och sätt bas-URL till din backend
const http = axios.create({
    baseURL: 'http://localhost:8081', // Backend server URL
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 5000,  // Timeout om du vill att förfrågningarna ska avslutas efter viss tid
});

// Använda instansen för att göra ett API-anrop
const generateRecipe = async (ingredients) => {
    try {
        const response = await http.post('/api/recipes/generate', {
            ingredients: ingredients.join(", "), // Skicka ingredienser som kommaseparerad sträng
            allergens: [] // Skicka allergener om tillgängligt
        });
        console.log(response.data);  // Visar receptet från backend (OpenAI)
    } catch (error) {
        console.error("Error generating recipe:", error);
    }
};

// Anropa funktionen med ingredienser för att testa
generateRecipe(["tomatoes", "garlic", "olive oil", "basil"]);
