package com.vijai.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryRequest {

    private Long id;

    @NotNull
    @NotBlank
    private String categoryName;

    @NotNull
    private boolean def;
}
