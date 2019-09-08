package com.vijai.blog.payload;

import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String place;
    private String teaser;
    private String title;
    private Date startDate;
    private Date endDate;
    private String body;
    private boolean publish;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Instant updateDateTime;
    private List<String> categories;
}
