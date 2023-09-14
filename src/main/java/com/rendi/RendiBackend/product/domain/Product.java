package com.rendi.RendiBackend.product.domain;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.common.BaseTimeEntity;
import com.rendi.RendiBackend.colour.Colour;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String title;
    private String price;
    private String detailUrl;
    private Long hits;
    private String keywords;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colour_id")
    private Colour colour;

    @ElementCollection
    @CollectionTable(name = "product_imgs", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> productImgUrl;



}