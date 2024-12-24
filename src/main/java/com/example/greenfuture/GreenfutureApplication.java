package com.example.greenfuture;

import com.example.greenfuture.model.Users;
import com.example.greenfuture.repository.UsersRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GreenfutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenfutureApplication.class, args);
	}
	@Bean
	CommandLineRunner initDatabase(UsersRepo userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Check if the user already exists
			if (!userRepository.findByEmail("Mananjaysth111@gmail.com").isPresent()) {
				Users user = new Users();
				user.setName("Admin User");
				user.setEmail("Mananjaysth111@gmail.com");
				user.setPassword(passwordEncoder.encode("admin123"));
				user.setContact("9860252886");
				user.setDesignation(null);
				user.setStatus("active");
				user.setGender("Male");
				user.setUserRole("admin");
				userRepository.save(user);
				System.out.println("Admin user created!");
			}
		};
	}
}
