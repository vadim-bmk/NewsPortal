package com.dvo.NewsPortal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "news")
@Data
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> commentsList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    public void addComments(Comment comment) {
        comment.setNews(this);
        commentsList.add(comment);
    }

}
