package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.aop.SecurityCheck;
import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.mapper.UserMapper;
import com.dvo.NewsPortal.service.UserService;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertUserRequest;
import com.dvo.NewsPortal.web.model.response.ErrorResponse;
import com.dvo.NewsPortal.web.model.response.ModelListResponse;
import com.dvo.NewsPortal.web.model.response.NewsResponse;
import com.dvo.NewsPortal.web.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get users",
            description = "Get all users",
            tags = {"users", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ModelListResponse<UserResponse> findAll(@Valid PaginationRequest request) {
        Page<User> userPage = userService.findAll(request.pageRequest());
        return ModelListResponse.<UserResponse>builder()
                .totalCount(userPage.getTotalElements())
                .data(userPage.stream().map(userMapper::userToResponse).toList())
                .build();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get user",
            description = "Get user by ID. Return ID, name, list of news",
            tags = {"users", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @SecurityCheck
    public UserResponse findById(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long userId) {
        return userMapper.userToResponse(userService.findById(userId));
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create user",
            description = "Create user",
            tags = {"users", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public UserResponse createUser(@RequestBody UpsertUserRequest request, @RequestParam List<RoleType> roleType) {
        User newUser = userService.save(userMapper.requestToUser(request), roleType.stream().map(Role::from).collect(Collectors.toList()));
        return userMapper.userToResponse(newUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create user",
            description = "Create user",
            tags = {"users", "id"}
    )
    public UserResponse createUser(@RequestBody UpsertUserRequest request) {
        User newUser = userService.save(userMapper.requestToUser(request), List.of(Role.from(RoleType.ROLE_USER)));
        return userMapper.userToResponse(newUser);
    }

    @PutMapping("/admin/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update user",
            description = "Update user by ID",
            tags = {"users", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @SecurityCheck
    public UserResponse updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long userId,
                                   @RequestBody UpsertUserRequest request,
                                   @RequestParam List<RoleType> roleType) {
        User user = userService.update(userMapper.requestToUser(request), userId, roleType.stream().map(Role::from).collect(Collectors.toList()));
        return userMapper.userToResponse(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update user",
            description = "Update user by ID",
            tags = {"users", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @SecurityCheck
    public UserResponse updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long userId,
                                   @RequestBody UpsertUserRequest request) {
        User user = userService.update(userMapper.requestToUser(request), userId, null);
        return userMapper.userToResponse(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user",
            description = "Delete user by ID",
            tags = {"users", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @SecurityCheck
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable Long userId) {
        userService.deleteById(userId);
    }


}
