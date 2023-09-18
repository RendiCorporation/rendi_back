package com.rendi.RendiBackend.wish.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishListResponse {

    private Long ProductId;
    private Long price;
    private Long brandId;
    private String title;
    private List<String> imgUrls;
    private String href;
}
