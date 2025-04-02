package com.company.project.dto.response;

import com.company.project.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private CategorySummary parentCategory;
    private List<CategorySummary> subCategories;
    private List<ProductSummary> products;
    private Category.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummary {
        private Long id;
        private String name;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummary {
        private Long id;
        private String name;
        private String description;
    }

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentCategory(category.getParentCategory() != null ? CategorySummary.builder()
                        .id(category.getParentCategory().getId())
                        .name(category.getParentCategory().getName())
                        .description(category.getParentCategory().getDescription())
                        .build() : null)
                .subCategories(category.getSubCategories() != null ? 
                        category.getSubCategories().stream()
                                .map(subCategory -> CategorySummary.builder()
                                        .id(subCategory.getId())
                                        .name(subCategory.getName())
                                        .description(subCategory.getDescription())
                                        .build())
                                .collect(Collectors.toList()) : null)
                .products(category.getProducts() != null ?
                        category.getProducts().stream()
                                .map(product -> ProductSummary.builder()
                                        .id(product.getId())
                                        .name(product.getName())
                                        .description(product.getDescription())
                                        .build())
                                .collect(Collectors.toList()) : null)
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(category.getCreatedBy())
                .lastModifiedBy(category.getLastModifiedBy())
                .build();
    }
} 