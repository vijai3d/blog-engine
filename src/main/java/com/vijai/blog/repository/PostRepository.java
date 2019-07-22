package com.vijai.blog.repository;

import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByDomain(Domain domain, Pageable pageable);

    Page<Post> findByDomainAndCreatedBy(Domain domain, Long userId, Pageable pageable);

    List<Post> findAllByDomainAndUpdatedAt(Domain domain, Instant updatedAt);
}
