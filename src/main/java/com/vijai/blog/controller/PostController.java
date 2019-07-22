package com.vijai.blog.controller;

import com.vijai.blog.exception.BadRequestException;
import com.vijai.blog.model.Domain;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PollResponse;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.security.CurrentUser;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.service.PostService;
import com.vijai.blog.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public PagedResponse<PostResponse> getPosts(@RequestHeader("domain") Domain domain,
                                       @CurrentUser UserPrincipal currentUser,
                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return postService.getAllPolls(domain, currentUser, page, size);
    }


}