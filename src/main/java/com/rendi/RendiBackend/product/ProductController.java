package com.rendi.RendiBackend.product;

import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.dto.*;
import com.rendi.RendiBackend.product.repository.ProductRepository;
import com.rendi.RendiBackend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("")
    public StringResponse createProducts(@RequestBody List<ProductSaveRequest> requestList) {
        productService.autoCreateProducts(requestList);
        return new StringResponse("상품 리스트 DB 저장 완료");
    }
    @PatchMapping("/hits/update")
    public StringResponse updateHits(@RequestBody HitsUpdateRequest request) {
        productService.updateHits(request);
        return new StringResponse("hits changed");
    }

    @GetMapping("/guest/new") //신상품 for guest (not loginned)
    public List<ProductGuestResponse> getProductsSortedByDateGuest(@RequestParam(required = false) String categoryName) {
        return productService.getNewProductsGuest(categoryName);
    }
    @GetMapping("/new") //신상품 for user (loginned)
    public List<ProductUserResponse> getProductsSortedByDateUser(@RequestParam(required = false) String categoryName) {
        return productService.getNewProductsUser(categoryName);
    }
    @GetMapping("/guest/all") // 전체 상품 for guest (not loginned)
    public List<ProductGuestResponse> getAllProductsGuest() {
        return productService.getAllProductsGuest();
    }
    @GetMapping("/all") // 전체 상품 for user (loginned)
    public List<ProductUserResponse> getAllProductsUser() {
        return productService.getAllProductsUser();
    }
    @GetMapping("/guest/best") // best 상품 for guest (not loginned)
    public List<ProductGuestResponse> getBestProductsGuest(@RequestParam(required = false) String categoryName){
        return productService.getBestProductsGuest(categoryName);
    }
    @GetMapping("/best") // best 상품 for user (loginned)
    public List<ProductUserResponse> getBestProductsUser(@RequestParam(required = false) String categoryName){
        return productService.getBestProductsUser(categoryName);
    }

    @GetMapping("/today")
    public List<ProductUserResponse> getRecommendProductsUser(@RequestParam List<Long> recommendBrandIds){
        return productService.getRecommendProductsUser(recommendBrandIds);
    }
    @GetMapping("/recent") //최근 본 상품
    public List<ProductUserResponse> getRecentProducts(@RequestParam List<Long> recentProductIds) {
        return productService.getRecentProducts(recentProductIds);
    }

//    @GetMapping("/filter")
//    public List<ProductGuestResponse> SortedByFilter(@RequestParam(required = false) String sortName,
//                                                     @RequestParam(required = false) String parentCategory,
//                                                     @RequestParam(required = false) String childCategory,
//                                                     @RequestParam(required = false) String colourName,
//                                                     @RequestParam(required = false) String priceTag){
//
//        Specification<Product> spec = Specification.where(
//                        ProductSpecification.hasParentCategory(parentCategory))
//                .and(ProductSpecification.hasChildCategory(childCategory))
//                .and(ProductSpecification.hasColour(colourName))
//                .and(ProductSpecification.inPriceRange(priceTag));
//
//        List<Product> products = productRepository.findAll(spec);
//
//        // Convert products into ProductGuestResponse objects and return.
//    }



}
