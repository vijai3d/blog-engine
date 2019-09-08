package com.vijai.blog.service;

import com.vijai.blog.exception.BadRequestException;
import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Post;
import com.vijai.blog.model.User;
import com.vijai.blog.payload.PagedResponse;
import com.vijai.blog.payload.PostResponse;
import com.vijai.blog.payload.UserResponse;
import com.vijai.blog.repository.UserRepository;
import com.vijai.blog.util.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements ValidationInterface{
    @Autowired
    private UserRepository userRepository;


    public PagedResponse<UserResponse> getAllUsers(Domain domain, int page, int size) {
        validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "username");
        Page<User> users = userRepository.findAllByDomain(domain, pageable);

        return getUserResponsePagedResponse(users);
    }

    private PagedResponse<UserResponse> getUserResponsePagedResponse(Page<User> users) {
        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        List<UserResponse> userResponses = users.map(user -> ModelMapper.mapUserToUserResponse(user)).getContent();

        return new PagedResponse<>(userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    public void deleteUser(Domain domain, Long id) {
        User user = userRepository.findByDomainAndId(domain, id).orElseThrow(() -> new BadRequestException("Cannot delete user with such id."));
        userRepository.delete(user);
        log.debug("User with id: {} deleted", id);
    }
}
