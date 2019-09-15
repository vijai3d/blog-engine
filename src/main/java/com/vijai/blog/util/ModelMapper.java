package com.vijai.blog.util;

import com.vijai.blog.model.*;
import com.vijai.blog.payload.*;
import com.vijai.blog.security.UserPrincipal;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);
        Set<String> roles = creator.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName(), roles);
        pollResponse.setCreatedBy(creatorSummary);

        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
    }

    public static PostResponse mapPostsToPostResponse(Post post, User editor) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setPlace(post.getPlace());
        postResponse.setTeaser(post.getTeaser());
        postResponse.setTitle(post.getTitle());
        postResponse.setStartDate(post.getStartDate());
        postResponse.setEndDate(post.getEndDate());
        postResponse.setBody(post.getBody());
        postResponse.setPublish(post.isPublished());
        UserSummary userSummary = new UserSummary(editor.getId(), editor.getUsername(), editor.getName(), mapRoles(editor.getRoles()));
        postResponse.setCreatedBy(userSummary);
        postResponse.setCreationDateTime(post.getCreatedAt());
        List<String> categories = post.getCategories().stream().map(cat -> mapCategoryToResponse(cat).getCategoryName()).collect(Collectors.toList());
        postResponse.setUpdateDateTime(post.getUpdatedAt());
        postResponse.setCategories(categories);
        return postResponse;
    }

    public static void mapPostRequestToPost(PostRequest postRequest, Post post) {
        post.setTitle(postRequest.getTitle());
        post.setPlace(postRequest.getPlace());
        post.setTeaser(postRequest.getTeaser());
        post.setStartDate(postRequest.getStartDate());
        post.setEndDate(postRequest.getEndDate());
        post.setBody(postRequest.getBody());
        post.setPublished(postRequest.isPublish());
    }

    public static Collection<String> mapRoles(Set<Role> roles) {
        List<String> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.add(role.getName().name());
        }
        return collection;
    }

    public static CategoryResponse mapCategoryToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setCategoryName(category.getName());
        response.setDef(category.isDef());
        response.setPostCount(category.getPosts().size());
        return response;
    }

    public static Category mapCategoryRequestToCategory(Category category, CategoryRequest categoryRequest) {
        category.setName(categoryRequest.getCategoryName());
        category.setDef(categoryRequest.isDef());
        return category;
    }

    public static UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        userResponse.setCreationDateTime(user.getCreatedAt());
        userResponse.setUpdateDateTime(user.getUpdatedAt());
        userResponse.setRole(appendUserRoles(user));
        userResponse.setEnabled(user.isEnabled());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    private static Collection<String> appendUserRoles(User user) {
        return user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList());
    }
}
