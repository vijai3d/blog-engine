package com.vijai.blog.model;

import com.vijai.blog.model.audit.UserDateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "posts")
public class Post extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(max = 150)
    @Column(name = "place")
    private String place;

    @NotBlank
    @Size(max = 350)
    @Column(name = "teaser")
    private String teaser;

    @Lob
    @Column(name = "body")
    private String body;
}
