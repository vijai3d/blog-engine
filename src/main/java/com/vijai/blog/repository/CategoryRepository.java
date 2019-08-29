package com.vijai.blog.repository;

import com.vijai.blog.model.Category;
import com.vijai.blog.model.Domain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAllByDomain(Domain domain);

    void removeByDomainAndId(Domain domain, Long id);

    Category findByDomainAndId(Domain domain, Long id);
}
