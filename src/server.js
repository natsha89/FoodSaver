require('dotenv').config();  // Ladda miljövariabler från .env-fil

const express = require('express');
const axios = require('axios');
const cors = require('cors');
const app = express();

// Ladda OpenAI API-nyckel från miljövariabler
const OPENAI_API_KEY = process.env.OPENAI_API_KEY;

// Tillåt endast din frontend-applikation (localhost:3000) att skicka förfrågningar
app.use(cors({
    origin: 'http://localhost:3000',  // Din frontend-url
    methods: ['GET', 'POST', 'PUT', 'DELETE'],  // Tillåt specifika metoder
    allowedHeaders: ['Content-Type', 'Authorization'],  // Tillåt dessa headers
}));

app.use(express.json());  // För att kunna läsa JSON-data från frontend

// API-rutt för att generera ett recept baserat på användarens ingredienser
app.post('/api/generate-recipe', async (req, res) => {
    const { ingredients } = req.body;  // Användarens ingredienser skickas via body

    if (!ingredients || ingredients.length === 0) {
        return res.status(400).json({ error: 'No ingredients provided.' });
    }

    // Skapa en prompt för att skicka till OpenAI
    const prompt = `Create a recipe using the following ingredients: ${ingredients.join(", ")}`;

    try {
        const response = await axios.post(
            'https://api.openai.com/v1/completions', // OpenAI GPT-3 endpoint
            {
                model: 'text-davinci-003',  // Du kan byta till GPT-4 om du har tillgång till det
                prompt: prompt,
                max_tokens: 300,
                temperature: 0.7, // Justera kreativiteten på receptet
            },
            {
                headers: {
                    'Authorization': `Bearer ${OPENAI_API_KEY}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        // Hämta och skicka tillbaka receptet från OpenAI
        const aiResponse = response.data.choices[0].text.trim();
        res.json({ recipe: aiResponse });
    } catch (error) {
        console.error("Error calling OpenAI:", error.response ? error.response.data : error.message);
        res.status(500).json({ error: 'Failed to generate recipe from OpenAI.' });
    }
});

// En enkel API-rutt för att testa om servern är igång
app.get('/', (req, res) => {
    res.send('Backend is running');
});

const PORT = 8081;
app.listen(PORT, () => {
    console.log(`Backend server is running on http://localhost:${PORT}`);
});
