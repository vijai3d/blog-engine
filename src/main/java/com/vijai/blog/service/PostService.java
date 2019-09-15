package com.vijai.blog.service;

import com.vijai.blog.exception.ResourceNotFoundException;
import com.vijai.blog.model.Category;
import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Post;
import com.vijai.blog.model.User;
import com.vijai.blog.payload.CategoryRequest;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PostRequest;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.repository.CategoryRepository;
import com.vijai.blog.repository.PostRepository;
import com.vijai.blog.repository.UserRepository;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService implements ValidationInterface{

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public PagedResponse<PostResponse> getAllPosts(Domain domain, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Posts
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Post> posts = postRepository.findAllByDomain(domain, pageable);

        return getPostResponsePagedResponse(posts);
    }

    public PagedResponse<PostResponse> getAllPublishedPosts(Domain domain, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Posts
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Post> posts = postRepository.findAllByDomainAndPublishedTrue(domain, pageable);

        return getPostResponsePagedResponse(posts);
    }

    private PagedResponse<PostResponse> getPostResponsePagedResponse(Page<Post> posts) {
        if (posts.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), posts.getNumber(),
                    posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
        }

        List<PostResponse> postResponses = posts.map(post -> {
            return ModelMapper.mapPostsToPostResponse(post, userRepository.getOne(post.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(postResponses, posts.getNumber(),
                posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
    }

    public PagedResponse<PostResponse> getPostsCreatedBy(Domain domain, String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all posts created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Post> posts = postRepository.findByDomainAndCreatedBy(domain, user.getId(), pageable);

        if (posts.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), posts.getNumber(),
                    posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
        }

        List<PostResponse> postResponses = posts
                .map(post -> ModelMapper.mapPostsToPostResponse(post, userRepository.getOne(post.getCreatedBy())))
                .getContent();

        return new PagedResponse<>(postResponses, posts.getNumber(),
                posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
    }

    public PostResponse getPostById(Domain domain, Long postId) {
        Post post = postRepository.findByDomainAndId(domain, postId);
        return ModelMapper.mapPostsToPostResponse(post, userRepository.getOne(post.getCreatedBy()));
    }

    public Post createPost(Domain domain, PostRequest postRequest) {
        Post post = new Post();
        post.setDomain(domain);
        ModelMapper.mapPostRequestToPost(postRequest, post);
        post.setCategories(getCategoriesFromPostRequest(domain, postRequest));
        postRepository.save(post);
        return post;
    }

    public Post updatePost(Domain domain, Long postId, PostRequest postRequest) {
        Post post = postRepository.findByDomainAndId(domain, postId);
        ModelMapper.mapPostRequestToPost(postRequest, post);
        post.setCategories(getCategoriesFromPostRequest(domain, postRequest));
        postRepository.save(post);
        return post;
    }

    public void deletePost(Domain domain, Long postId) {
        Post post = postRepository.findByDomainAndId(domain, postId);
        postRepository.delete(post);
    }

    private List<Category> getCategoriesFromPostRequest(Domain domain, PostRequest postRequest) {
        return categoryRepository.findAllByDomainAndNameIn(domain, postRequest.getCategories());
    }
}
