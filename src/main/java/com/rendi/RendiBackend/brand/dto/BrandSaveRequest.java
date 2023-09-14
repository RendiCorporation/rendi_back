package com.rendi.RendiBackend.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandSaveRequest {

    private String brandName;
    private String iconUrl;
    private String bannerUrl;
}
