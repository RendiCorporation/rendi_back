package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.search.domain.Popular;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopularRepository extends JpaRepository<Popular, Long> {
    Optional<Popular> findByKeyword(String keyword);
}
