package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
