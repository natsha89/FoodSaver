package com.natasha.foodsaver.service;

import com.natasha.foodsaver.exception.EmailNotVerifiedException;
import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.exception.UserNotFoundException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    // Logger för att logga viktiga händelser (t.ex. registrering, inloggning)
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository; // Dependency injection för UserRepository

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // För att hashning av lösenord

    @Autowired
    private EmailService emailService; // För att skicka verifieringsmail

    private static final int VERIFICATION_LINK_EXPIRY_MINUTES = 60; // Tidsgräns för verifieringslänken (i minuter)


    // Registrera en ny användare
    public User register(User user) {
        // Kontrollera om email redan är registrerat
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.warn("Försök att registrera med en redan existerande email: {}", user.getEmail());
            throw new UserAlreadyExistsException("Email är redan registrerat.");
        }

        // Kryptera lösenordet
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generera verifieringstoken
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiration(LocalDateTime.now().plusHours(1)); // Sätt en utgångstid för token

        // Spara användaren i databasen
        userRepository.save(user);

        // Skicka verifieringsmail
        emailService.sendVerificationEmail(user.getEmail(), token);

        logger.info("Användare registrerad framgångsrikt: {}", user.getEmail());
        return user; // Returnera den registrerade användaren
    }

    // Inloggning av användare
    public User login(String email, String password) {
        // Hämta användaren baserat på email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Försök att logga in med en icke-existerande email: {}", email);
            throw new UserNotFoundException("Email inte funnen.");
        }

        // Kontrollera om lösenordet stämmer
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Fel lösenord för email: {}", email);
            throw new RuntimeException("Fel lösenord.");
        }

        // Kontrollera om email är verifierad
        if (!user.isEmailVerified()) {
            logger.warn("Försök att logga in med en icke-verifierad email: {}", email);
            throw new EmailNotVerifiedException("Email inte verifierad. Kontrollera din inkorg.");
        }

        logger.info("Användare inloggad framgångsrikt: {}", email);
        return user; // Returnera den inloggade användaren
    }

    // Radera användarkonto
    public void deleteAccount(String userId) {
        // Kontrollera om användaren existerar
        if (!userRepository.existsById(userId)) {
            logger.warn("Försök att radera en icke-existerande användare med ID: {}", userId);
            throw new UserNotFoundException("Användaren finns inte.");
        }

        // Radera användaren
        userRepository.deleteById(userId);
        logger.info("Användarkonto raderat framgångsrikt: {}", userId);
    }

    // Verifiera email med hjälp av token
    public boolean verifyEmail(String token) {
        // Hämta användaren baserat på verifieringstoken
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            // Kontrollera om token har inte gått ut
            if (user.getVerificationTokenExpiration().isAfter(LocalDateTime.now())) {
                // Markera användarens email som verifierad
                user.setEmailVerified(true);
                user.setVerificationToken(null); // Ta bort token efter verifiering
                user.setVerificationTokenExpiration(null); // Ta bort expiration date
                userRepository.save(user); // Spara den verifierade användaren
                logger.info("Email verifierad framgångsrikt för användare: {}", user.getEmail());
                return true;
            } else {
                // Om token har gått ut
                logger.warn("Verifieringstoken har gått ut för email: {}", user.getEmail());
                throw new RuntimeException("Verifieringstoken har gått ut. Vänligen begär en ny.");
            }
        }

        logger.warn("Ogiltig verifieringstoken: {}", token);
        return false; // Returnera false om token inte är giltig
    }

    // Skicka en ny verifieringslänk
    public void resendVerificationEmail(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Generera en ny verifieringstoken
            String token = generateVerificationToken();
            user.setVerificationToken(token);
            user.setVerificationTokenExpiration(LocalDateTime.now().plusMinutes(VERIFICATION_LINK_EXPIRY_MINUTES)); // Token utgår efter en timme
            userRepository.save(user); // Spara användaren med den nya token
            emailService.sendVerificationEmail(user.getEmail(), token); // Skicka ny verifieringslänk
        }
    }

    // Generera verifieringstoken
    private String generateVerificationToken() {
        // En enkel metod för att generera token (kan ersättas med mer avancerade tekniker som JWT)
        return Long.toHexString(System.currentTimeMillis()); // En token baserat på systemets nuvarande tid
    }
}
