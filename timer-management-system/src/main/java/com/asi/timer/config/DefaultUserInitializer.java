package com.asi.timer.config;

import com.asi.timer.components.JwtRequestFilter;
import com.asi.timer.enums.Role;
import com.asi.timer.model.db.DBUser;
import com.asi.timer.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.count() == 0) {

            // Add initial users

            String simonPassword = System.getenv("DEFAULT_USER_SIMON_PASSWORD");
            String markusPassword = System.getenv("DEFAULT_USER_MARKUS_PASSWORD");
            String stefanPassword = System.getenv("DEFAULT_USER_STEFAN_PASSWORD");

            if( simonPassword == null || markusPassword == null || stefanPassword == null) {
                logger.error("Default user passwords are not set in environment variables. Please set them.");
                return;
            }

            userRepository.save(new DBUser("simon", passwordEncoder.encode(simonPassword), Role.JUDGE));
            userRepository.save(new DBUser("markus", passwordEncoder.encode(markusPassword), Role.JUDGE));
            userRepository.save(new DBUser("stefan", passwordEncoder.encode(stefanPassword), Role.ADMIN));

            logger.info("Initialized default users.");

        } else {
            logger.info("User table already initialized, skipping default insert.");
        }
    }
}
