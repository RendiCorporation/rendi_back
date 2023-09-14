package com.rendi.RendiBackend.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProductErrorCode {

    PRODUCT_NOT_FOUND_BY_ID("해당 상품의 아이디로 상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND_BY_BRAND_ID("브랜드 아이디로 상품을 찾을 수 없습니다."),
    PRODUCT_TITLE_DUPLICATED("해당 상품과 같은 이름을 가진 상품이 있습니다."),
    COLOUR_NOT_FOUND_BY_COLOUR_NAME("해당 색상이 없습니다."),
    CATEGORY_NOT_FOUND_BY_CATEGORY_NAME("카테고리 이름이 존재하지 않습니다.");
    private String defaultMessage;
}
