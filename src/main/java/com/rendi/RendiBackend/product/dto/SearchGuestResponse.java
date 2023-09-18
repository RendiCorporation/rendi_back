package com.rendi.RendiBackend.product.dto;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.product.dto.ProductGuestResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchGuestResponse {
    private List<Long> productIds;
    private List<ProductGuestResponse> responseList;
}

