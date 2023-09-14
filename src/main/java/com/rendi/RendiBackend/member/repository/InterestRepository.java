package com.rendi.RendiBackend.member.repository;

import com.rendi.RendiBackend.member.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByFieldContaining(String keyword);
}

