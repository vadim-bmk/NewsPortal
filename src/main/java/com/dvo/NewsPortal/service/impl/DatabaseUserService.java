package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseRoleRepository;
import com.dvo.NewsPortal.repository.DatabaseUserRepository;
import com.dvo.NewsPortal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseUserService implements UserService {
    private final DatabaseUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DatabaseRoleRepository roleRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info("Call findAll in DatabaseUserService");
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        log.info("Call findById in DatabaseUserService by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден!", id)));
    }

    @Override
    public User findByUsername(String name) {
        log.info("Call findByName in DatabaseUserService by name: {}", name);
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден!", name)));
    }

    @Override
    @Transactional
    public User save(User user, List<Role> roles) {
        log.info("Call save in DatabaseUserService by user: {}", user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        for (Role r : roles) {
            r.setUser(user);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user, Long id, List<Role> roles) {
        log.info("Call update in DatabaseUserService by ID: {}", id);
        User existedUser = findById(id);
        if (user.getUsername() != null) existedUser.setUsername(user.getUsername());
        if (user.getPassword() != null) existedUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if (roles != null) {
            existedUser.getRoles().forEach(r ->
                    roleRepository.deleteById(r.getId()));
            existedUser.getRoles().clear();
            roles.forEach(r -> {
                existedUser.getRoles().add(r);
                r.setUser(existedUser);
            });
        }


        return userRepository.save(existedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteByID in DatabaseUserService by ID: {}", id);
        userRepository.deleteById(id);
    }

//    @Override
//    public User saveWithNews(User user, List<News> news) {
//        User savedUser = userRepository.save(user);
//
//        for(News newsOne : news){
//            newsOne.setUser(savedUser);
//            var savedNews = newsRepository.save(newsOne);
//        }
//        return null;
//    }
}
