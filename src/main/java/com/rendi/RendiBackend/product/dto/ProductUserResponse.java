package com.rendi.RendiBackend.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserResponse {
    private Long productId;

    private String price;

    private Long brandId;

    private String title;

    private boolean wishYN;
    private List<String> imgUrls;
    private String href;

}
