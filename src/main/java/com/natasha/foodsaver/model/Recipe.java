package com.natasha.foodsaver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

// Markerar att denna klass representerar ett dokument i MongoDB-samlingen "recipes"
@Document(collection = "recipes")
public class Recipe {

    @Id
    private String id;  // Unikt ID för receptet
    private String name;  // Namnet på receptet
    private String instructions;  // Instruktioner för hur man lagar receptet
    private List<String> ingredients;  // Lista över ingredienser som ingår i receptet
    private String userId;  // ID för användaren som har skapat eller äger receptet


    // Konstruktor för att skapa ett recept med namn och instruktioner
    // Notera att ingredienser inte används i denna konstruktor, så det kan behövas en uppdatering
    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients; // här borde ingredienser sättas korrekt, men denna konstruktor saknar en parameter för ingredienser
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
