package com.rendi.RendiBackend.member.repository;

import com.rendi.RendiBackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    List<Member> findAllByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByPassword(String password);
}
