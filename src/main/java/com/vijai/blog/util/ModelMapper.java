package com.vijai.blog.util;

import com.vijai.blog.model.Poll;
import com.vijai.blog.model.Post;
import com.vijai.blog.model.Role;
import com.vijai.blog.model.User;
import com.vijai.blog.payload.ChoiceResponse;
import com.vijai.blog.payload.PollResponse;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.payload.UserSummary;
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
        postResponse.setBody(post.getBody());
        UserSummary userSummary = new UserSummary(editor.getId(), editor.getUsername(), editor.getName(), mapRoles(editor.getRoles()));
        postResponse.setCreatedBy(userSummary);
        postResponse.setCreationDateTime(post.getCreatedAt());
        postResponse.setUpdateDateTime(post.getUpdatedAt());
        return postResponse;
    }

    public static Collection<String> mapRoles(Set<Role> roles) {
        List<String> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.add(role.getName().name());
        }
        return collection;
    }


}
