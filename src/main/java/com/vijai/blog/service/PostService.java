package com.vijai.blog.service;

import com.vijai.blog.exception.BadRequestException;
import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Poll;
import com.vijai.blog.model.Post;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PollResponse;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.repository.PostRepository;
import com.vijai.blog.repository.UserRepository;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.util.AppConstants;
import com.vijai.blog.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PagedResponse<PostResponse> getAllPolls(Domain domain, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Post> posts = postRepository.findAllByDomain(domain, pageable);

        if(posts.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), posts.getNumber(),
                    posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
        }


        List<PostResponse> postResponses = posts.map(post -> {
            return ModelMapper.mapPostsToPostResponse(post, userRepository.getOne(post.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(postResponses, posts.getNumber(),
                posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
