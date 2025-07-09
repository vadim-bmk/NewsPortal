package com.dvo.NewsPortal.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
@Data
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<News> newsList = new ArrayList<>();

    public void addNews(News news) {
        news.setCategory(this);
        newsList.add(news);
    }
}
