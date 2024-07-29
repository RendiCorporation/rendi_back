package com.rendi.RendiBackend.product;

import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.product.dto.*;
import com.rendi.RendiBackend.repositories.ProductRepository;
import com.rendi.RendiBackend.product.service.ProductService;
import com.rendi.RendiBackend.product.dto.SearchGuestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
//    @GetMapping("/new")
//    public Page<ProductUserResponse> getProductsSortedByDateUser(@RequestParam(required = false) String categoryName,
//                                                                 @PageableDefault(size = 16) Pageable pageable) {
//        return productService.getNewProductsUser(categoryName, pageable);
//    }
    @GetMapping("/guest/all") // 전체 상품 for guest (not loginned) + filter
    public List<ProductGuestResponse> getAllProductsGuest(@RequestParam(required = false) String sortName,
                                                          @RequestParam(required = false) String parentCategory,
                                                          @RequestParam(required = false) String childCategory,
                                                          @RequestParam(required = false) String colourName,
                                                          @RequestParam(required = false) Long minPrice,
                                                          @RequestParam(required = false) Long maxPrice) {
        return productService.getAllProductsGuest(sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
    }
    @GetMapping("/all") // 전체 상품 for user (loginned) + filter
    public List<ProductUserResponse> getAllProductsUser(@RequestParam(required = false) String sortName,
                                                        @RequestParam(required = false) String parentCategory,
                                                        @RequestParam(required = false) String childCategory,
                                                        @RequestParam(required = false) String colourName,
                                                        @RequestParam(required = false) Long minPrice,
                                                        @RequestParam(required = false) Long maxPrice) {
        return productService.getAllProductsUser(sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
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
    @GetMapping("/category") // category 상품 for user (loginned)
    public List<ProductUserResponse> getProductsByCategoryUser(@RequestParam(required = false) String parentName,
                                                         @RequestParam(required = false) String childName){
        return productService.getProductsByCategoryUser(parentName, childName);
    }
    @GetMapping("/guest/category") // category 상품 for guest (not loginned)
    public List<ProductGuestResponse> getProductsByCategoryGuest(@RequestParam(required = false) String parentName,
                                                               @RequestParam(required = false) String childName){
        return productService.getProductsByCategoryGuest(parentName, childName);
    }

    @GetMapping("/guest/search/keyword")
    public List<SearchGuestResponse> getProductsByKeywordGuest(@RequestParam String keywordName){
        return productService.searchByKeywordGuest(keywordName);
    }


    @GetMapping("/guest/search/keyword/filter")
    public List<ProductGuestResponse> searchProductsFilter(@RequestParam List<Long> productIds,
                                                           @RequestParam(required = false) String sortName,
                                                           @RequestParam(required = false) String parentCategory,
                                                           @RequestParam(required = false) String childCategory,
                                                           @RequestParam(required = false) String colourName,
                                                           @RequestParam(required = false) Long minPrice,
                                                           @RequestParam(required = false) Long maxPrice){
        return productService.searchProductsFilter(productIds, sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
    }
//    @GetMapping("/image")
//    public List<ProductUserResponse> getProductsByImage(@RequestParam String imgUrl){
//        return searchService.searchByImage(imgUrl);

    @GetMapping("/filter")
    public List<ProductGuestResponse> SortedByFilter(@RequestParam(required = false) String sortName,
                                                     @RequestParam(required = false) String parentCategory,
                                                     @RequestParam(required = false) String childCategory,
                                                     @RequestParam(required = false) String colourName,
                                                     @RequestParam(required = false) Long minPrice,
                                                     @RequestParam(required = false) Long maxPrice){

        return productService.searchProductAndSort(sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
    }

    //using gemini api



}
