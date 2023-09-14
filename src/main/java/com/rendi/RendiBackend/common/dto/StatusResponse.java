package com.rendi.RendiBackend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusResponse {
    private String msg;
    private Object data;
}