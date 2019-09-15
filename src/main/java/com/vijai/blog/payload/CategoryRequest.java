package com.vijai.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryRequest {
    @NotNull
    @NotBlank
    private String categoryName;

    private String oldValue;
}
