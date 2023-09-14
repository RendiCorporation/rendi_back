package com.rendi.RendiBackend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewLoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;




}