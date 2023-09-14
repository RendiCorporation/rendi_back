package com.rendi.RendiBackend.member.repository;

import com.rendi.RendiBackend.member.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
