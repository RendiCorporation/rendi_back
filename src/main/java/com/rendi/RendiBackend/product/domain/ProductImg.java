//package com.rendi.RendiBackend.product.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//@Setter
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Entity
//public class ProductImg {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_img_id")
//    private Long id;
//
//    @Setter
//    @Column(name="origin_url")
//    private String originUrl;
//    @Setter
//    @Column(name="store_url")
//    private String storeUrl;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Product product;
//
//    public ProductImg(String storeUrl, Product product) {
//        this.storeUrl = storeUrl;
//        this.product = product;
//    }
//
//}
