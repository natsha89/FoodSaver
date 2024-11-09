package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")  // Anger att denna klass ska mappar till en MongoDB-samling (collection) med namnet "recipes"
public class Recipe {

    @Id  // Anger att detta är primärnyckeln för objektet i MongoDB
    private String id;
    private String name;  // Namnet på receptet
    private String instructions;  // Instruktioner för att laga receptet
    private List<String> foodItem;  // Lista med matvaror (ingredienser) som ingår i receptet
    private String userId;  // Användar-ID för att koppla receptet till en användare
    private String title;  // Titel på receptet
    private String description;  // En kort beskrivning av receptet

    // All-arguments konstruktor
    // Konstruktor som används för att skapa ett recept med alla parametrar
    public Recipe(String id, String name, String instructions, List<String> foodItem,
                  String userId, String title, String description) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItem;
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    // Konstruktor för att skapa recept med bara namn, instruktioner och ingredienser
    // Denna konstruktor används när man inte behöver sätta alla fält
    public Recipe(String name, String instructions, List<String> foodItems) {
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItems; // Tillåter att man sätter ingredienser om så önskas
    }

    // Konstruktor för att initialisera endast beskrivningen
    public Recipe(String description) {
        this.description = description;
    }

    // Getters och Setters (metoder för att hämta och sätta värden)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(List<String> foodItem) {
        this.foodItem = foodItem;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString metod
    // Denna metod används för att skapa en strängrepresentation av objektet
    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instructions='" + instructions + '\'' +
                ", foodItem=" + foodItem +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
