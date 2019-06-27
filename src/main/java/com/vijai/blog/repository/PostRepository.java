package com.vijai.blog.repository;

import com.vijai.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDomain(String domain);

    List<Post> findAllByDomainAndUpdatedAt(String domain, Instant updatedAt);
}
