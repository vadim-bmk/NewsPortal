package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseRoleRepository extends JpaRepository<Role, Long> {
    void deleteAllByUserId(Long userId);
}
