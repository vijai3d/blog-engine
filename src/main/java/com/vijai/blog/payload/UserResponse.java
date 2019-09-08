package com.vijai.blog.payload;

import lombok.Data;

import java.time.Instant;
import java.util.Collection;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private Collection<String> role;
    private Instant creationDateTime;
    private Instant updateDateTime;
    private boolean enabled;
    private String email;
}
