package com.vijai.blog.controller;

import com.vijai.blog.model.Domain;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.security.CurrentUser;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.service.PostService;
import com.vijai.blog.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return postService.getAllPublishedPosts(domain, page, size);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@RequestHeader("domain") Domain domain,
                                                   @PathVariable(value = "id") Long postId) {
        return postService.getPostById(domain, postId);
    }


}
