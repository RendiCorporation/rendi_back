package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.colour.Colour;
import com.rendi.RendiBackend.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,  JpaSpecificationExecutor<Product> {
    Optional<Product> findByTitle(String title);
    List<Product> findByBrandId(Long brandId);
    List<Product> findByCategory(Category category);
//    List<Product> findByColour(Colour colour);
    List<Product> findByCategoryOrderByCreatedAtDesc(Category category);
    List<Product> findByCategoryOrderByHitsDesc(Category category);
    List<Product> findByIdIn(List<Long> ids);
    List<Product> findByColoursContains(Colour colour);
    List<Product> findByCategoryIn(List<Category> categories);
    List<Product> findByCategoryInOrderByHitsDesc(List<Category> categories);
    List<Product> findByCategoryInOrderByCreatedAtDesc(List<Category> categories);
    List<Product> findByBrandIdAndCategoryIn(Long brandId, List<Category> categories);

}
