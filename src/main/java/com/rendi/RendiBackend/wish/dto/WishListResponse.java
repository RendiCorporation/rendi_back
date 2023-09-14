package com.rendi.RendiBackend.wish.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishListResponse {

    private Long ProductId;
    private String price;
    private Long brandId;
    private String title;
}
