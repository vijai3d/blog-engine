package com.vijai.blog.payload;

import com.vijai.blog.model.Domain;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private Collection<String> role;
}
