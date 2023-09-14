package com.rendi.RendiBackend.member.domain;

import com.rendi.RendiBackend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Enumerated(EnumType.STRING) // DB 저장 시 Enum 값 설정 (디폴트 = int형 숫자) -> 문자로 변경
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column
    private String emailAgreeYn;

    @Column
    private String phoneAgreeYn;


    @Builder
    public Member(String username, String password, String email,Provider provider,String emailAgreeYn, String phoneAgreeYn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.USER;
        this.provider = provider;
        this.emailAgreeYn = emailAgreeYn;
        this.phoneAgreeYn = phoneAgreeYn;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void modifyPw(String password) {
        this.password = password;
    }
}
