package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseRoleRepository;
import com.dvo.NewsPortal.repository.DatabaseUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseUserServiceTest {
    @InjectMocks
    private DatabaseUserService userService;

    @Mock
    private DatabaseUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DatabaseRoleRepository roleRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user")
                .password("12345")
                .build();
    }

    @Test
    void testFindAll() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findById(1L);

        assertEquals(user, result);
    }

    @Test
    void testFindById_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        User result = userService.findByUsername("user");

        assertEquals(user, result);
    }

    @Test
    void testFindByUsername_whenNotFound() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername("user"));
    }

    @Test
    void testSave() {
        when(passwordEncoder.encode("12345")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(user, List.of(Role.from(RoleType.ROLE_USER)));

        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdate() {
        User updatedUser = User.builder()
                .username("newUsername")
                .password("12345")
                .build();

        Role role = Role.from(RoleType.ROLE_USER);
        role.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("12345")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.update(updatedUser, 1L, List.of(role));

        assertEquals("newUsername", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteById() {
        userRepository.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

}
