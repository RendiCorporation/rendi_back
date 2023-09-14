package com.rendi.RendiBackend.product.service;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.brand.exception.BrandErrorCode;
import com.rendi.RendiBackend.brand.exception.BrandException;
import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.category.CategoryRepository;
import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.service.MemberService;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.dto.*;
import com.rendi.RendiBackend.product.exception.ProductErrorCode;
import com.rendi.RendiBackend.product.exception.ProductException;
import com.rendi.RendiBackend.brand.repository.BrandRepository;
import com.rendi.RendiBackend.product.repository.ProductRepository;
import com.rendi.RendiBackend.colour.Colour;
import com.rendi.RendiBackend.colour.ColourRepository;
import com.rendi.RendiBackend.wish.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ColourRepository colourRepository;
    private final CategoryRepository categoryRepository;
    private final WishService wishService;
    private final MemberService memberService;
    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public void autoCreateProducts(List<ProductSaveRequest> requestList){
        for (ProductSaveRequest request : requestList) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(()-> new BrandException(BrandErrorCode.BRAND_NOT_FOUND_BY_ID));
            if(productRepository.findByTitle(request.getTitle()).isPresent()){
                throw new ProductException(ProductErrorCode.PRODUCT_TITLE_DUPLICATED);
            }
            Colour colour = colourRepository.findByColourName(request.getColourName())
                    .orElseThrow(()-> new ProductException(ProductErrorCode.COLOUR_NOT_FOUND_BY_COLOUR_NAME));
            Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            Product product = request.toProduct();
            product.setBrand(brand);
            product.setColour(colour);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Transactional
    public void updateHits(HitsUpdateRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID));
        product.setHits(request.getHits());
//        productRepository.save(product);
    }

    @Transactional
    public List<ProductGuestResponse> getNewProductsGuest(String categoryName){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) { // 모든 상품 조회
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else { // 특정 카테고리의 상품만 조회
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products = productRepository.findByCategoryOrderByCreatedAtDesc(category);
        }
        for (Product product : products) {
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getNewProductsUser(String categoryName) {
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) { // 모든 상품 조회
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else { // 특정 카테고리의 상품만 조회
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products = productRepository.findByCategoryOrderByCreatedAtDesc(category);
        }
        for (Product product : products) {
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    wishYN, product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }

    @Transactional
    public List<ProductGuestResponse> getBestProductsGuest(String categoryName){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "hits"));
        } else {
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products = productRepository.findByCategoryOrderByHitsDesc(category);
        }
        for (Product product : products) {
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getBestProductsUser(String categoryName){
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "hits"));
        } else {
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products = productRepository.findByCategoryOrderByHitsDesc(category);
        }
        for (Product product : products) {
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    wishYN, product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getAllProductsUser(){
        List<ProductUserResponse> dtos = new ArrayList<>();
        // Todo : 무슨 기준으로 ALL?
        List<Product> products = productRepository.findAll();
        Member member = memberService.findCurrentMember();
        for (Product product : products) {
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                    ,wishYN,product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    @Transactional
    public List<ProductGuestResponse> getAllProductsGuest(){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                    ,product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getRecommendProductsUser(List<Long> recommendBrandIds){
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        List<Product> allProducts = new ArrayList<>();

        for(Long brandId : recommendBrandIds){
            List<Product> products = productRepository.findByBrandId(brandId);
            allProducts.addAll(products);
        }
        Collections.shuffle(allProducts);
        List<Product> randomProducts = allProducts.subList(0, Math.min(allProducts.size(), 12));

        for(Product product : randomProducts){
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                    ,wishYN, product.getProductImgUrl(), product.getDetailUrl()));
        }

        return dtos;
    }
    public List<ProductUserResponse> getRecentProducts(List<Long> productIds){
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        for(Long productId : productIds){
            Product product = productRepository.findById(productId)
                            .orElseThrow(()-> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID));
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(productId, product.getPrice(), product.getBrand().getId(), product.getTitle()
            ,wishYN, product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }
    public List<Product> sortedByFilter(String sortName, String parentCategory, String childCategory,
                                        String colourName, String priceTag) {
        if (parentCategory != null){
            Category category = categoryRepository.findByCategoryName(parentCategory)
                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            return productRepository.findByCategory(category);
        }
        if (childCategory != null){
            Category category = categoryRepository.findByCategoryName(childCategory)
                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            return productRepository.findByCategory(category);
        }
        if (colourName != null){
            Colour colour = colourRepository.findByColourName(colourName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.COLOUR_NOT_FOUND_BY_COLOUR_NAME));
            return productRepository.findByColour(colour);
        }


        return productRepository.findAll();
    }
    //    public void ciderUrlstoJpg(List<ProductImgSaveRequest> requestList){
//        for(ProductImgSaveRequest request : requestList){
//            String originUrl = request.getOriginUrl();
//            Product product = productRepository.findById(request.getProductId())
//                    .orElseThrow(()->new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID));
//            String storeUrl = "images/#cider" + product.getTitle() +
//        }
//        String Url =
//        String imagePath = "ciderImages/이름.jpg"+;
//        URL imageUrl = new URL(imageLink);
//        InputStream inputStream = imageUrl.openStream();
//        OutputStream outputStream = new FileOutputStream("이미지_경로/이름.jpg");
//
//        byte[] buffer = new byte[2048];
//        int length;
//        while ((length = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, length);
//        }
//
//        inputStream.close();
//        outputStream.close();
//    }



}
