package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.auth.domain.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    MemberRefreshToken findByUsername(String username);
    MemberRefreshToken findByUsernameAndRefreshToken(String username, String refreshTokenValue);
}
