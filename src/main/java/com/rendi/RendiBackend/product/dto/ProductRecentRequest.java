package com.rendi.RendiBackend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecentRequest {
    private List<Long> recentProductIds;
}
