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
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final int VERIFICATION_LINK_EXPIRY_MINUTES = 60; // För att ange hur lång tid verifieringslänken är giltig


    public User register(User user) {
        // Check if the email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.warn("Attempt to register with an existing email: {}", user.getEmail());
            throw new UserAlreadyExistsException("Email is already in use.");
        }

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiration(LocalDateTime.now().plusHours(1));

        // Save user and include allergies and dietary preferences
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token);

        logger.info("User registered successfully: {}", user.getEmail());
        return user;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Login attempt for non-existent email: {}", email);
            throw new UserNotFoundException("Email not found.");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Incorrect password attempt for email: {}", email);
            throw new RuntimeException("Incorrect password.");
        }
        if (!user.isEmailVerified()) {
            logger.warn("Login attempt for unverified email: {}", email);
            throw new EmailNotVerifiedException("Email not verified. Please check your inbox.");
        }
        logger.info("User logged in successfully: {}", email);
        return user;
    }

    public void deleteAccount(String userId) {
        if (!userRepository.existsById(userId)) {
            logger.warn("Attempt to delete non-existent user ID: {}", userId);
            throw new UserNotFoundException("User not found.");
        }
        userRepository.deleteById(userId);
        logger.info("User account deleted successfully: {}", userId);
    }

    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            if (user.getVerificationTokenExpiration().isAfter(LocalDateTime.now())) {
                user.setEmailVerified(true);
                user.setVerificationToken(null); // Clear token after verification
                user.setVerificationTokenExpiration(null); // Clear expiration date
                userRepository.save(user);
                logger.info("Email verified successfully for user: {}", user.getEmail());
                return true;
            } else {
                logger.warn("Verification token expired for email: {}", user.getEmail());
                throw new RuntimeException("Verification token has expired. Please request a new one.");
            }
        }
        logger.warn("Invalid verification token: {}", token);
        return false;
    }
    public void resendVerificationEmail(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = generateVerificationToken();
            user.setVerificationToken(token);
            user.setVerificationTokenExpiration(LocalDateTime.now().plusMinutes(VERIFICATION_LINK_EXPIRY_MINUTES)); // Token utgår efter en timme
            userRepository.save(user);
            emailService.sendVerificationEmail(user.getEmail(), token); // Skicka ny verifieringslänk
        }
    }

    private String generateVerificationToken() {
        // Här kan du använda en mer avancerad metod för att generera token (t.ex. JWT)
        return Long.toHexString(System.currentTimeMillis());
    }
}