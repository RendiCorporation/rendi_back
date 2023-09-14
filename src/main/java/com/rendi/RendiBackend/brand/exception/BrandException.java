package com.rendi.RendiBackend.brand.exception;

import com.rendi.RendiBackend.product.exception.ProductErrorCode;
import lombok.Getter;

@Getter
public class BrandException extends RuntimeException{
    private BrandErrorCode errorCode;
    private String errorMessage;

    public BrandException(BrandErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public BrandException(BrandErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}