package com.dvo.NewsPortal.configuration;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.repository.DatabaseUserRepository;
import com.dvo.NewsPortal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final DatabaseUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            log.info("Инициализация администратора...");

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword("admin");

            Role adminRole = new Role();
            adminRole.setAuthority(RoleType.ROLE_ADMIN);

            userService.save(admin, List.of(adminRole));
            log.info("Администратор создан: {} / {}", adminUsername, "admin");
        }
    }
}
