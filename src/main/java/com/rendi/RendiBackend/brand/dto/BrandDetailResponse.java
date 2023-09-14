package com.rendi.RendiBackend.brand.dto;


import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.product.dto.ProductUserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandDetailResponse {
    private Long brandId;
    private String brandName;
    private String brandIconUrl;
    private String brandBannerUrl;
    private Long totalWishes;

    private List<ProductUserResponse> responseList;

    public BrandDetailResponse(Brand brand, Long totalWishes, List<ProductUserResponse> responseList) {
        this.brandId = brand.getId();
        this.brandName = brand.getBrandName();
        this.brandIconUrl = brand.getIconUrl();
        this.brandBannerUrl = brand.getBannerUrl();
        this.totalWishes = totalWishes;
        this.responseList = responseList;

    }


}
