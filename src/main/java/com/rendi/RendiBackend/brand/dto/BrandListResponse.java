package com.rendi.RendiBackend.brand.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandListResponse {
    private Long brandId;
    private String brandName;
    private String brandIconUrl;



}
