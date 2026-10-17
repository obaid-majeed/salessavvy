package com.appplication.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.appplication.entity.Users;
import com.appplication.repository.UsersRepository;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner seedAdmin(
            UsersRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            String adminUsername = "admin";

            if (!userRepository.existsByUsername(adminUsername)) {

                Users admin = new Users();
                admin.setName("Admin");
                admin.setUsername(adminUsername);
                admin.setPassword(
                        passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println(
                        "Admin user created: admin / admin123");
            }
        };
    }
}
