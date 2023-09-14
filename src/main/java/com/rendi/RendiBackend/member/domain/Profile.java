package com.rendi.RendiBackend.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String nickname;
    private String email;

    private String phonenum;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String sex;

    @Setter
    private String imgUrl;

    @OneToMany(mappedBy = "profile")
    private List<Interest> interests = new ArrayList<>();

    @Builder
    public Profile(Member member, String nickname, String email, String phonenum, LocalDate birth, String sex) {
        this.member = member;
        this.nickname = nickname;
        this.email = email;
        this.phonenum = phonenum;
        this.birth = birth;
        this.sex = sex;
    }

    public Profile updateProfile(String email, String nickname, LocalDate birth, String phonenum) {
        this.email = email;
        this.nickname = nickname;
        this.birth = birth;
        this.phonenum = phonenum;

        return this;
    }
}
