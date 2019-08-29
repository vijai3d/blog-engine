package com.vijai.blog.model;

import com.vijai.blog.model.audit.UserDateAudit;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

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

    @Basic
    @Column(name = "start_date")
    private Date startDate;

    @Basic
    @Column(name = "end_date")
    private Date endDate;

    @Lob
    @Column(name = "body")
    private String body;

    @Column(name = "published", columnDefinition = "boolean default false", nullable = false)
    private boolean published;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}
