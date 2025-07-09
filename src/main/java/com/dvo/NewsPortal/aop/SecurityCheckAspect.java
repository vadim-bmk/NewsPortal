package com.dvo.NewsPortal.aop;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.service.impl.DatabaseUserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityCheckAspect {

    private final DatabaseUserService userService;

    @Around("@annotation(SecurityCheck) && args(userDetails, requestedUserId, ..)")
    public Object securityCheck(ProceedingJoinPoint pjp, UserDetails userDetails, Long requestedUserId) {

        Long currentUserId = userService.findByUsername(userDetails.getUsername()).getId();
        List<Role> currentUserRoles = userService.findByUsername(userDetails.getUsername()).getRoles();
        Set<RoleType> currentUserRoleTypes = new HashSet<>();
        for (Role r : currentUserRoles) {
            currentUserRoleTypes.add(r.getAuthority());
        }
        ;

        boolean securityCheck = false;

        if (currentUserRoleTypes.contains(RoleType.ROLE_ADMIN) || currentUserRoleTypes.contains(RoleType.ROLE_MODERATOR)) {
            securityCheck = true;
        } else {
            if (Objects.equals(currentUserId, requestedUserId)) securityCheck = true;

        }

        if (!securityCheck) {
            throw new AccessDeniedException("Not access!!!");
        }

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}
