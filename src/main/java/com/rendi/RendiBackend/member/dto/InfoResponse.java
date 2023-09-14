package com.rendi.RendiBackend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse {
    private String email;
    private String nickname;
    private String username;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String phone;
}
