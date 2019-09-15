package com.vijai.blog.controller;

import com.vijai.blog.model.Category;
import com.vijai.blog.model.Domain;
import com.vijai.blog.model.Post;
import com.vijai.blog.payload.ApiResponse;
import com.vijai.blog.payload.CategoryRequest;
import com.vijai.blog.payload.CategoryResponse;
import com.vijai.blog.payload.PostRequest;
import com.vijai.blog.repository.CategoryRepository;
import com.vijai.blog.security.CurrentUser;
import com.vijai.blog.security.UserPrincipal;
import com.vijai.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> list(@RequestHeader("domain") Domain domain) {
        return categoryService.list(domain);
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestHeader("domain") Domain domain,
                                        @Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.create(domain, categoryRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(category.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Category Created Successfully"));
    }

    @PutMapping
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@RequestHeader("domain") Domain domain,
                                            @Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(domain, categoryRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{Id}")
                .buildAndExpand(category.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Category Updated Successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@RequestHeader("domain") Domain domain,
                                            @PathVariable(value = "id") Long id) {
        categoryService.delete(domain, id);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Category Deleted Successfully"));
    }

    @PostMapping("/default/{id}")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<?> makeCategoryDefault(@RequestHeader("domain") Domain domain,
                                            @PathVariable(value = "id") Long id) {
        categoryService.makeDefault(domain, id);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Category is default now"));
    }
}
