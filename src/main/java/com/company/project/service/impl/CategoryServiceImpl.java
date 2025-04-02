package com.company.project.service.impl;

import com.company.project.dto.request.CategoryRequest;
import com.company.project.entity.Category;
import com.company.project.exception.ResourceNotFoundException;
import com.company.project.repository.CategoryRepository;
import com.company.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        if (request.getParentCategoryId() != null) {
            Category parentCategory = getCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        if (request.getParentCategoryId() != null) {
            // Prevent circular reference
            if (id.equals(request.getParentCategoryId())) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }
            Category parentCategory = getCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }
        
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        
        // Check if category has products
        if (categoryRepository.hasProducts(id)) {
            throw new IllegalStateException("Cannot delete category with products");
        }
        
        categoryRepository.delete(category);
    }
} 