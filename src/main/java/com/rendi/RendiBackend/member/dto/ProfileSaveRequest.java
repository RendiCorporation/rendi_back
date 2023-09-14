package com.rendi.RendiBackend.member.dto;

import com.rendi.RendiBackend.member.domain.Profile;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileSaveRequest {

    @NotBlank
    private String nickname;
    @NotBlank
    private String email;
    private String phonenum;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String sex;

    private List<String> interests;

    public Profile toProfile() {
        return Profile.builder()
                .nickname(nickname)
                .email(email)
                .phonenum(phonenum)
                .birth(birth)
                .sex(sex)
                .build();
    }

//    public void updateProfile(Profile profile) {
//        profile.updateProfile(this.nickname, this.email, this.phonenum, this.birth, this.sex);
//    }

}
