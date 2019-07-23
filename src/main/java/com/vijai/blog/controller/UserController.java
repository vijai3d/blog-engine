package com.vijai.blog.controller;

import com.vijai.blog.exception.ResourceNotFoundException;
import com.vijai.blog.model.Domain;
import com.vijai.blog.model.User;
import com.vijai.blog.payload.*;
import com.vijai.blog.repository.PollRepository;
import com.vijai.blog.repository.PostRepository;
import com.vijai.blog.repository.UserRepository;
import com.vijai.blog.repository.VoteRepository;
import com.vijai.blog.security.CurrentUser;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.service.PollService;
import com.vijai.blog.service.PostService;
import com.vijai.blog.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('USER') or hasRole('ADMIN')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        Set<String> roles = currentUser.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toSet());
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), roles);
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestHeader("domain") Domain domain,
                                                              @RequestParam(value = "username") String username) {

        Boolean isAvailable = !userRepository.existsByDomainAndUsername(domain, username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestHeader("domain") Domain domain,
                                                           @RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByDomainAndEmail(domain, email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long postCount = postRepository.countByCreatedBy(user.getId());
        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), postCount, pollCount, voteCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/posts")
    public PagedResponse<PostResponse> getUserPosts(@RequestHeader("domain") Domain domain,
                                                    @CurrentUser UserPrincipal currentUser,
                                                    @PathVariable(value = "username") String username,
                                                    @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return postService.getPostsCreatedBy(domain, username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }
}
