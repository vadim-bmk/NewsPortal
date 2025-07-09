package com.dvo.NewsPortal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<News> newsList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> commentsList = new ArrayList<>();


    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    public void addNews(News news) {
        news.setUser(this);
        newsList.add(news);
    }

    public void addComment(Comment comment) {
        comment.setUser(this);
        commentsList.add(comment);
    }
}
