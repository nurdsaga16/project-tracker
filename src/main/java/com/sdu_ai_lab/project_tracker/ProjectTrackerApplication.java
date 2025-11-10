package com.sdu_ai_lab.project_tracker;

import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableScheduling
@SpringBootApplication
public class ProjectTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTrackerApplication.class, args);
	}

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                var encoder = new BCryptPasswordEncoder();

                User admin = User.builder()
                        .fullName("Admin User")
                        .email("admin@example.com")
                        .password(encoder.encode("admin123"))
                        .description("System Administrator")
                        .position(UserPosition.FULLSTACK_DEVELOPER) // или другой enum, если есть
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Default admin user created!");
            }
        };
    }

}
