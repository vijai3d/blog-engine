package com.vijai.blog.controller;

import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Post;
import com.vijai.blog.payload.ApiResponse;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PostRequest;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.security.CurrentUser;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.service.PostService;
import com.vijai.blog.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public PagedResponse<PostResponse> getPublishedPosts(@RequestHeader("domain") Domain domain,
                                                @CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return postService.getAllPublishedPosts(domain, page, size);
    }

    @GetMapping("/all")
    public PagedResponse<PostResponse> getAllPosts(@RequestHeader("domain") Domain domain,
                                                @CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return postService.getAllPosts(domain, page, size);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@RequestHeader("domain") Domain domain,
                                                   @PathVariable(value = "id") Long postId) {
        return postService.getPostById(domain, postId);
    }

    @PostMapping(consumes = "application/json;charset=UTF-8")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createPost(@RequestHeader("domain") Domain domain,
                                        @Valid @RequestBody PostRequest postRequest) {
      Post post = postService.createPost(domain, postRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Post Created Successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePost(@RequestHeader("domain") Domain domain,
                                        @PathVariable(value = "id") Long postId,
                                        @Valid @RequestBody PostRequest postRequest) {
        Post post = postService.updatePost(domain, postId, postRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Post Updated Successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@RequestHeader("domain") Domain domain,
                                        @PathVariable(value = "id") Long postId) {
        postService.deletePost(domain, postId);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Post Deleted Successfully"));
    }

}
