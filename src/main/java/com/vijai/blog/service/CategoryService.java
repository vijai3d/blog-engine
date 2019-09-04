package com.vijai.blog.service;

import com.vijai.blog.model.Category;
import com.vijai.blog.model.Domain;
import com.vijai.blog.payload.CategoryRequest;
import com.vijai.blog.payload.CategoryResponse;
import com.vijai.blog.repository.CategoryRepository;
import com.vijai.blog.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> list(Domain domain) {
        List<Category> categories = categoryRepository.findAllByDomain(domain);
        return categories.stream().map(ModelMapper::mapCategoryToResponse).collect(Collectors.toList());
    }

    public Category create(Domain domain, CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setDomain(domain);
        category.setName(categoryRequest.getCategoryName());
        category.setDef(categoryRequest.isDef());
        categoryRepository.save(category);
        return category;
    }

    public void delete(Domain domain, Long id) {
        Category category = categoryRepository.findByDomainAndId(domain, id);
        categoryRepository.delete(category);
    }

    public Category updateCategory(Domain domain, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByDomainAndId(domain, categoryRequest.getId());
        ModelMapper.mapCategoryRequestToCategory(category, categoryRequest);
        categoryRepository.save(category);
        return category;
    }

    public void makeDefault(Domain domain, Long id) {
        List<Category> categories = categoryRepository.findAllByDomain(domain);
        categories.forEach(category -> category.setDef(false));
        Category categoryDefault = categoryRepository.findByDomainAndId(domain, id);
        categoryDefault.setDef(true);
        categoryRepository.save(categoryDefault);
     }
}
