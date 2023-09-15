package com.rendi.RendiBackend.member.dto;

import com.rendi.RendiBackend.member.domain.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse {
    private String email;
    private String nickname;
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String phone;
    private List<String> interests;
}
