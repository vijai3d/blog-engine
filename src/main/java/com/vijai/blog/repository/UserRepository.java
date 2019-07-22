package com.vijai.blog.repository;

import com.vijai.blog.model.Domain;
import com.vijai.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByDomainAndEmail(Domain domain, String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByDomainAndUsernameOrEmail(Domain domain, String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByDomainAndUsername(Domain domain, String username);

    Boolean existsByDomainAndEmail(Domain domain, String email);

    Optional<User> findByDomainAndId(Domain domain, Long id);
}
