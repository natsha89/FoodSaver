package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.FoodItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Märker klassen som en tjänstkomponent för Spring, så att den kan användas för beroendeinjektion
public class FoodItemAlertService {

    // Metod som kollar om ett livsmedelsobjekt innehåller allergener som användaren är allergisk mot
    public String getAllergyAlert(FoodItem foodItem, List<String> userAllergies) {
        // Om matvaran innehåller allergener som användaren är allergisk mot, returnera en varning
        return foodItem.checkAllergies(userAllergies) ?
                "Varning: Livsmedlet innehåller allergener som du är allergisk mot." : "";
    }

    // Metod som kollar om ett livsmedelsobjekt är nära att gå ut
    public String getExpirationAlert(FoodItem foodItem) {
        // Om matvaran är nära utgångsdatumet, returnera en varning
        return foodItem.scheduleExpirationNotification() ?
                "Notis: Livsmedlet närmar sig sitt utgångsdatum." : "";
    }
}
