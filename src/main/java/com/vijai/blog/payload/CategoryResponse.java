package com.vijai.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryResponse {

    @NotNull
    private String categoryName;

    @NotNull
    private boolean def;

    private int postCount;
}
