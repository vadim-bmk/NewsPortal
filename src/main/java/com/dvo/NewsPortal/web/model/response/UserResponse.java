package com.dvo.NewsPortal.web.model.response;

import com.dvo.NewsPortal.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private List<ShortNewsResponse> newsList = new ArrayList<>();
    private List<RoleType> roles;

}
