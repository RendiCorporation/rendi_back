package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String brandName);
}
