package com.rendi.RendiBackend.auth.service;

import com.rendi.RendiBackend.auth.domain.CustomUserDetails;
import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.exception.MemberErrorCode;
import com.rendi.RendiBackend.member.exception.MemberException;
import com.rendi.RendiBackend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return CustomUserDetails.create(member);
    }
}
