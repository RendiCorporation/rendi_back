package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.member.domain.Interest;
import com.rendi.RendiBackend.member.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByFieldContaining(String keyword);
    Optional<Interest> findByProfile(Profile profile);
}

