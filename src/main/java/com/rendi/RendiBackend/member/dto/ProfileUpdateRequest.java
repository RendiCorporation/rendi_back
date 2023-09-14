package com.rendi.RendiBackend.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileUpdateRequest {
    private String email;
    private String nickname;
    private LocalDate birth;
    private String phonenum;
}
