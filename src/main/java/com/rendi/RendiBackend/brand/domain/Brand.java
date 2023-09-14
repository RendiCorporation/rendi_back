package com.rendi.RendiBackend.brand.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_icon")
    private String iconUrl; //브랜드 이미지

    @Column(name = "brand_banner")
    private String bannerUrl; //브랜드 이미지

    public Brand(String brandName, String iconUrl, String bannerUrl) {
        this.brandName = brandName;
        this.iconUrl = iconUrl;
        this.bannerUrl = bannerUrl;
    }
}
