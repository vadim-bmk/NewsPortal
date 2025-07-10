package com.dvo.NewsPortal.web.controller;


import com.dvo.NewsPortal.configuration.SecurityConfiguration;
import com.dvo.NewsPortal.entity.Role;
import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.mapper.UserMapper;
import com.dvo.NewsPortal.security.UserDetailsServiceImpl;
import com.dvo.NewsPortal.service.UserService;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertUserRequest;
import com.dvo.NewsPortal.web.model.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({UserDetailsServiceImpl.class, SecurityConfiguration.class})
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponse userResponse;
    private final String URL = "/api/user";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user")
                .password("12345")
                .build();

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("user");
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testFndAll() throws Exception {
        PaginationRequest request = PaginationRequest.builder().pageNumber(0).pageSize(10).build();
        Page<User> page = new PageImpl<>(List.of(user));

        when(userService.findAll(request.pageRequest())).thenReturn(page);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get(URL)
                        .param("pageNumber", request.getPageNumber().toString())
                        .param("pageSize", request.getPageSize().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].username").value("user"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testFindById() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testCreateUserAdmin() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder().username("user").password("12345").build();
        List<RoleType> roles = List.of(RoleType.ROLE_ADMIN);
        List<Role> roleEntities = roles.stream().map(Role::from).toList();

        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.save(user, roleEntities)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post(URL + "/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("roleType", "ROLE_ADMIN"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testCreateUserAdmin_withUnauthorizedRole() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder().username("user").password("12345").build();
        List<RoleType> roles = List.of(RoleType.ROLE_ADMIN);
        List<Role> roleEntities = roles.stream().map(Role::from).toList();

        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.save(user, roleEntities)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post(URL + "/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("roleType", "ROLE_ADMIN"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateUser() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder().username("user").password("12345").build();

        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.save(user, List.of(Role.from(RoleType.ROLE_USER)))).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testUpdateUserAdmin() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder().username("user").password("12345").build();
        List<RoleType> roles = List.of(RoleType.ROLE_ADMIN);
        List<Role> roleEntities = roles.stream().map(Role::from).toList();

        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.update(user, 1L, roleEntities)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(put(URL + "/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("roleType", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));

    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateUserWithoutRoles() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder()
                .username("user")
                .password("12345")
                .build();

        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.update(user, 1L, null)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());
    }
}
