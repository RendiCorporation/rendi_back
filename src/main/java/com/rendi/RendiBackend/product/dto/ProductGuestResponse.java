package com.rendi.RendiBackend.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGuestResponse {
    private Long productId;

    private Long price;

    private Long brandId;

    private String title;

    private List<String> imgUrls;
    private String href;

//    public static List<ProductNewResponse> toNewDtoList(List<Product> entities, Brand brand, List<String> imgUrls) {
//        List<ProductNewResponse> dtos = new ArrayList<>();
//
//        for (Product entity : entities)
//            dtos.add(new ProductNewResponse(entity.getId(),entity.getPrice(), brand.getId(), entity.getTitle(), ));
//
//        return dtos;
//    }
}
