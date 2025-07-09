package com.dvo.NewsPortal.mapper;

import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.web.model.request.UpsertUserRequest;
import com.dvo.NewsPortal.web.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = NewsMapper.class)
public interface UserMapper {
    User requestToUser(UpsertUserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UpsertUserRequest request);

    @Mapping(source = "newsList", target = "newsList")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "getUserRoles")
    UserResponse userToResponse(User user);

    @Named("getUserRoles")
   default List<RoleType> getUserRoles(List<Role> roles){
        return roles.stream().map(Role::getAuthority).collect(Collectors.toList());
    }
    @Named("getRequestUserRoles")
    default List<Role> getRequestUserRoles(List<RoleType> roles){
        return roles.stream().map(Role::from).collect(Collectors.toList());
    }

}
