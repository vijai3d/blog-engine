package com.vijai.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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

    private Date startDate;

    private Date endDate;

    @NotBlank
    private String body;

    @NotNull
    private boolean publish;

    @NotEmpty
    private List<String> categories;

}
