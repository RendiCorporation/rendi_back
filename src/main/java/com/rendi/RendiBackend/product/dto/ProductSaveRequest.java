package com.rendi.RendiBackend.product.dto;

import com.rendi.RendiBackend.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequest {
    private Long brandId;
    private String title; //product name
    private Long price;
    private String detailUrl;
    private Long hits;
    private String categoryName;
    private List<String> colourNames;
    private String keywordList;
    private List<String> productImgUrl;


    public Product toProduct() {
        return Product.builder()
                .title(title)
                .price(price)
                .detailUrl(detailUrl)
                .hits(hits)
                .keywords(keywordList)
                .productImgUrl(productImgUrl)
                .build();
    }

}
