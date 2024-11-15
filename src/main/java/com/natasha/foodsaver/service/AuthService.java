package com.natasha.foodsaver.service;

import com.natasha.foodsaver.exception.EmailNotVerifiedException;
import com.natasha.foodsaver.exception.InvalidCredentialsException;
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
import java.util.List;
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

    private static final int VERIFICATION_LINK_EXPIRY_MINUTES = 60;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.warn("Attempt to register with existing email: {}", user.getEmail());
            throw new UserAlreadyExistsException("Email already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String token = generateVerificationToken();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiration(LocalDateTime.now().plusMinutes(VERIFICATION_LINK_EXPIRY_MINUTES));
        user.setEmailVerified(false);

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);
        logger.info("User registered successfully: {}", user.getEmail());
        return user;
    }

    public void deleteAccount(String userId) {
        if (!userRepository.existsById(userId)) {
            logger.warn("Attempt to delete non-existent user with ID: {}", userId);
            throw new UserNotFoundException("User does not exist.");
        }

        userRepository.deleteById(userId);
        logger.info("User account deleted successfully: {}", userId);
    }

    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            logger.warn("Invalid verification token: {}", token);
            throw new RuntimeException("Invalid verification token.");
        }

        if (user.getVerificationTokenExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("Verification token expired for email: {}", user.getEmail());
            throw new RuntimeException("Verification token has expired. Please request a new one.");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiration(null);
        userRepository.save(user);

        logger.info("Email successfully verified for user: {}", user.getEmail());
        return true;
    }

    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Attempt to resend verification email to non-existent user: {}", email);
            throw new UserNotFoundException("User not found.");
        }

        if (user.isEmailVerified()) {
            logger.info("User's email is already verified: {}", email);
            return;
        }

        String token = generateVerificationToken();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiration(LocalDateTime.now().plusMinutes(VERIFICATION_LINK_EXPIRY_MINUTES));
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);
        logger.info("Resent verification email to: {}", email);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Attempt to login with non-existent email: {}", email);
            throw new UserNotFoundException("User not found.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Invalid credentials for email: {}", email);
            throw new InvalidCredentialsException("Incorrect email or password.");
        }

        if (!user.isEmailVerified()) {
            logger.warn("Attempt to login with unverified email: {}", email);
            throw new EmailNotVerifiedException("Email not verified. Please check your inbox.");
        }

        logger.info("User logged in successfully: {}", email);
        return user;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
