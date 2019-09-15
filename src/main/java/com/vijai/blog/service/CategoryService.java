package com.vijai.blog.service;

import com.vijai.blog.model.Category;
import com.vijai.blog.model.Domain;
import com.vijai.blog.payload.ApiResponse;
import com.vijai.blog.payload.CategoryRequest;
import com.vijai.blog.payload.CategoryResponse;
import com.vijai.blog.repository.CategoryRepository;
import com.vijai.blog.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        categoryRepository.save(category);
        return category;
    }

    public ResponseEntity<?> delete(Domain domain, String name) {
        Category category = categoryRepository.findByDomainAndName(domain, name);
        if (!category.isDef()) {
            categoryRepository.delete(category);
            return ResponseEntity.ok().body(new ApiResponse(true, "Category Deleted Successfully"));
        }
        return ResponseEntity.unprocessableEntity().body(new ApiResponse(true, "Category is default, can not be deleted"));
    }

    public Category updateCategory(Domain domain, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByDomainAndName(domain, categoryRequest.getOldValue());
        ModelMapper.mapCategoryRequestToCategory(category, categoryRequest);
        categoryRepository.save(category);
        return category;
    }

    public void makeDefault(Domain domain, String name) {
        List<Category> categories = categoryRepository.findAllByDomain(domain);
        categories.forEach(category -> category.setDef(false));
        Category categoryDefault = categoryRepository.findByDomainAndName(domain, name);
        categoryDefault.setDef(true);
        categoryRepository.save(categoryDefault);
     }
}
