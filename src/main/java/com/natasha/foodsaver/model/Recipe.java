package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")  // Anger att denna klass representerar ett dokument i MongoDB-samlingen "recipes"
public class Recipe {

    @Id
    private String id;  // Unikt ID för receptet
    private String name;  // Namnet på receptet
    private String instructions;  // Instruktioner för hur man lagar receptet
    private List<String> foodItem;  // Lista över ingredienser som ingår i receptet
    private String userId;  // ID för användaren som har skapat eller äger receptet
    private String title;  // Titel på receptet (kan vara en alternativ beskrivning eller rubrik)
    private String description;  // En kort beskrivning av receptet

    // Konstruktor för att skapa ett recept med namn, instruktioner och ingredienser
    public Recipe(String name, String instructions, List<String> foodItem) {
        this.name = name;
        this.instructions = instructions;
        this.foodItem = foodItem;
    }

    // Getters och setters för alla attribut
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
}
