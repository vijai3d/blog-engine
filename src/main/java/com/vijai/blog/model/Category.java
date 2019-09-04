package com.vijai.blog.model;

import com.vijai.blog.model.audit.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="category")
@Data
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "category_name", unique = true)
    private String name;

    @NotNull
    @Column(name = "def", columnDefinition = "boolean default false", nullable = false)
    private boolean def;

    @ManyToMany(mappedBy="categories")
    private List<Post> posts;
}
