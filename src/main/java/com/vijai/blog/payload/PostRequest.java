package com.vijai.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostRequest {

    @NotBlank
    @Size(min = 2, max = 150)
    private String title;

    @NotBlank
    @Size(min = 2, max = 150)
    private String place;

    @NotBlank
    @Size(min = 10, max = 350)
    private String teaser;

    @NotBlank
    private String body;

    @NotNull
    private boolean publish;

}
