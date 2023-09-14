package com.rendi.RendiBackend.member.dto;

import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocalJoinRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private ProfileSaveRequest profile;

    @NotBlank
    private String emailAgreeYn;

    @NotBlank
    private String phoneAgreeYn;

    public Member toMember() {
        return Member.builder()
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .email(profile.getEmail())
                .provider(Provider.LOCAL)
                .emailAgreeYn(emailAgreeYn)
                .phoneAgreeYn(phoneAgreeYn)
                .build();
    }


}