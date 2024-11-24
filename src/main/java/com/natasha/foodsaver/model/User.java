package com.natasha.foodsaver.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

@Document(collection = "users")  // Mappas till "users"-kollektionen i MongoDB
public class User implements UserDetails {

    @Id  // Primärnyckel för MongoDB
    private String id;

    @NotBlank(message = "Full Name is required")  // Fältet fullName är obligatoriskt
    private String fullName;

    @NotBlank(message = "Email is required")  // Fältet email är obligatoriskt
    @Email(message = "Email should be valid")  // Validerar att e-postadressen är korrekt
    private String email;

    @NotBlank(message = "Password is required")  // Fältet password är obligatoriskt
    @Size(min = 6, message = "Password must be at least 6 characters")  // Lösenordet måste vara minst 6 tecken långt
    private String password;

    private List<String> allergies;  // Lista över allergier
    private List<String> dietaryPreferences;  // Kostpreferenser (t.ex. vegetarisk, glutenfri)

    @DBRef(lazy = false)
    private List<Recipe> recipes= new ArrayList<>();  // or list of Recipe objects

    @DBRef(lazy = false)
    private List<FoodItem> foodItems = new ArrayList<>();

    private String verificationToken;  // Token för e-postverifiering
    private LocalDateTime verificationTokenExpiration;  // Utloppsdatum för verifieringstoken
    private boolean emailVerified = false;  // Flagga för att markera om e-post är verifierad

    // JWT token (om du vill spara den för session eller åtkomst)
    private String jwtToken;

    // Konstruktor utan argument (krävs för databindning)
    public User() {}

    // Fullständig konstruktor
    public User(String id, String fullName, String email, String password, List<String> allergies,
                List<String> dietaryPreferences, List<String> savedRecipes, String verificationToken,
                LocalDateTime verificationTokenExpiration, boolean emailVerified, String jwtToken) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.allergies = allergies;
        this.dietaryPreferences = dietaryPreferences;
        this.recipes = recipes;
        this.foodItems = foodItems;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiration = verificationTokenExpiration;
        this.emailVerified = emailVerified;
        this.jwtToken = jwtToken;
    }

    // Getters och setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<String> getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(List<String> dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }

    public List<Recipe> getRecipes() {return recipes;}

    public void setRecipes(List<Recipe> recipes) {this.recipes = recipes;}

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public LocalDateTime getVerificationTokenExpiration() { return verificationTokenExpiration; }
    public void setVerificationTokenExpiration(LocalDateTime verificationTokenExpiration) { this.verificationTokenExpiration = verificationTokenExpiration; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public List<FoodItem> getFoodItems() {return foodItems;}

    public void setFoodItems(List<FoodItem> foodItems) {this.foodItems = foodItems;}

    // JWT Token getter och setter
    public String getJwtToken() { return jwtToken; }
    public void setJwtToken(String jwtToken) { this.jwtToken = jwtToken; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", allergies=" + allergies +
                ", dietaryPreferences=" + dietaryPreferences +
                ", savedRecipes=" + recipes +
                ", verificationToken='" + verificationToken + '\'' +
                ", verificationTokenExpiration=" + verificationTokenExpiration +
                ", emailVerified=" + emailVerified +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }

    // Implementering av UserDetails metoder:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Här kan du definiera roller för användaren. Vi använder en statisk roll här.
        return Collections.singletonList(() -> "ROLE_USER");
    }

    @Override
    public String getUsername() {
        return email;  // Användarens email används som användarnamn
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Sätt som true om kontot inte ska kunna gå ut
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Sätt som true om kontot inte ska vara låst
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Sätt som true om autentiseringsuppgifterna inte ska gå ut
    }

    @Override
    public boolean isEnabled() {
        return true;  // Sätt som true om kontot ska vara aktivt
    }
}