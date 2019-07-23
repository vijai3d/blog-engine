package com.vijai.blog.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class PostResponse {
    private Long id;
    private String place;
    private String teaser;
    private String title;
    private String body;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Instant updateDateTime;
}
