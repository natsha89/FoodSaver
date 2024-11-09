package com.natasha.foodsaver.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

// Denna klass representerar användardata som lagras i en MongoDB-databas
@Document(collection = "users")  // Indikerar att denna klass mappar till "users"-kollektionen i MongoDB
public class User {

    @Id  // Detta annoterar att detta fält är primärnyckeln i databasen
    private String id;

    @NotBlank(message = "Full Name is required")  // Fältet fullName är obligatoriskt
    private String fullName;

    @NotBlank(message = "Email is required")  // Fältet email är obligatoriskt
    @Email(message = "Email should be valid")  // Validerar att e-postadressen har ett giltigt format
    private String email;

    @NotBlank(message = "Password is required")  // Fältet password är obligatoriskt
    @Size(min = 6, message = "Password must be at least 6 characters")  // Validerar att lösenordet är minst 6 tecken långt
    private String password;

    private List<String> allergies;  // Lista av allergier som användaren har
    private List<String> dietaryPreferences;  // Lista av användarens kostpreferenser (t.ex. vegetarian, glutenfri, etc.)
    private List<String> savedRecipes;  // Lista av användarens sparade recept (referenser till recept)

    private String verificationToken;  // Token för e-postverifiering
    private LocalDateTime verificationTokenExpiration;  // Utloppsdatum och tid för verifieringstoken
    private boolean emailVerified = false;  // Flagga som anger om användarens e-post är verifierad eller inte

    // No-argument constructor (Krävs för databindning och deserialisering)
    public User() {
    }

    // All-arguments constructor (Används för att skapa ett User-objekt med alla fält ifyllda)
    public User(String id, String fullName, String email, String password, List<String> allergies,
                List<String> dietaryPreferences, List<String> savedRecipes, String verificationToken,
                LocalDateTime verificationTokenExpiration, boolean emailVerified) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.allergies = allergies;
        this.dietaryPreferences = dietaryPreferences;
        this.savedRecipes = savedRecipes;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiration = verificationTokenExpiration;
        this.emailVerified = emailVerified;
    }

    // Getters and Setters (Tillåter andra klasser att få tillgång till och ändra fältvärdena)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(List<String> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public List<String> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiration() {
        return verificationTokenExpiration;
    }

    public void setVerificationTokenExpiration(LocalDateTime verificationTokenExpiration) {
        this.verificationTokenExpiration = verificationTokenExpiration;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    // toString-metod (Används för att skriva ut objektets data, exempelvis vid felsökning eller loggning)
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", allergies=" + allergies +
                ", dietaryPreferences=" + dietaryPreferences +
                ", savedRecipes=" + savedRecipes +
                ", verificationToken='" + verificationToken + '\'' +
                ", verificationTokenExpiration=" + verificationTokenExpiration +
                ", emailVerified=" + emailVerified +
                '}';
    }
}
