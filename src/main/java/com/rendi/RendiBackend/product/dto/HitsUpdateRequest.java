package com.rendi.RendiBackend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HitsUpdateRequest {
    private Long productId;
    private Long hits;
}
