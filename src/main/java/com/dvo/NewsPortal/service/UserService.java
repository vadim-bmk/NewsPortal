package com.dvo.NewsPortal.service;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User findByUsername(String name);

    User save(User user, List<Role> role);

    User update(User user, Long id, List<Role> role);

    void deleteById(Long id);
    //User saveWithNews(User user, List<News> news);

}
