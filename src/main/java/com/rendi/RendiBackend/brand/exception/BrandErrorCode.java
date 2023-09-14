package com.rendi.RendiBackend.brand.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum BrandErrorCode {

    BRAND_NOT_FOUND_BY_ID("해당 브랜드의 아이디로 브랜드를 찾을 수 없습니다."),
    BRAND_NOT_FOUND_BY_NAME("해당 브랜드의 이름으로 브랜드를 찾을 수 없습니다"),
    BRAND_NAME_DUPLICATED("브랜드 이름이 중복입니다."),
    BRAND_NOT_FOUND_BY_PRODUCTID("상품 아이디로 브랜드를 찾을 수 없습니다.");
    private String defaultMessage;
}
