package com.rendi.RendiBackend.product.dto;

import com.rendi.RendiBackend.product.domain.Product;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImgSaveRequest {
    private Long ProductId;
    private String originUrl;

    private String storeUrl;


}

