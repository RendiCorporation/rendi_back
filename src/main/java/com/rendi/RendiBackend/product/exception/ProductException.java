package com.rendi.RendiBackend.product.exception;

import com.rendi.RendiBackend.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException{
    private ProductErrorCode errorCode;
    private String errorMessage;

    public ProductException(ProductErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public ProductException(ProductErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}