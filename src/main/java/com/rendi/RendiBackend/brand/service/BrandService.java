package com.rendi.RendiBackend.brand.service;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.brand.dto.BrandDetailResponse;
import com.rendi.RendiBackend.brand.dto.BrandListResponse;
import com.rendi.RendiBackend.brand.dto.BrandSaveRequest;
import com.rendi.RendiBackend.brand.exception.BrandErrorCode;
import com.rendi.RendiBackend.brand.exception.BrandException;
import com.rendi.RendiBackend.brand.repository.BrandRepository;
import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.colour.Colour;
import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.service.MemberService;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.dto.ProductGuestResponse;
import com.rendi.RendiBackend.product.dto.ProductSaveRequest;
import com.rendi.RendiBackend.product.dto.ProductUserResponse;
import com.rendi.RendiBackend.product.exception.ProductErrorCode;
import com.rendi.RendiBackend.product.exception.ProductException;
import com.rendi.RendiBackend.product.repository.ProductRepository;
import com.rendi.RendiBackend.wish.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private final MemberService memberService;
    private final WishService wishService;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    @Transactional
    public Brand createBrand(BrandSaveRequest request) {
        return brandRepository.save(new Brand(request.getBrandName(), request.getIconUrl(), request.getBannerUrl()));
    }
    @Transactional
    public void autoCreateBrands(List<BrandSaveRequest> requestList){
        for (BrandSaveRequest request : requestList) {
            if(brandRepository.findByBrandName(request.getBrandName()).isPresent()){
                throw new BrandException(BrandErrorCode.BRAND_NAME_DUPLICATED);
            }
            brandRepository.save(new Brand(request.getBrandName(), request.getIconUrl(), request.getBannerUrl()));
        }
    }

    @Transactional
    public List<BrandListResponse> getAllBrands(){
        List<BrandListResponse> dtos = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            dtos.add(new BrandListResponse(brand.getId(), brand.getBrandName(), brand.getIconUrl()));
        }
        return dtos;
    }
    @Transactional
    public BrandDetailResponse getBrandDetails(String brandName){
        List<ProductUserResponse> dtos = new ArrayList<>();
        Brand brand = brandRepository.findByBrandName(brandName)
                .orElseThrow(()->new BrandException(BrandErrorCode.BRAND_NOT_FOUND_BY_NAME));
        List<Product> products = productRepository.findByBrandId(brand.getId());
        Member member = memberService.findCurrentMember();
        Long totalWishes = 0L;
        for (Product product : products){
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                    ,wishYN, product.getProductImgUrl(), product.getDetailUrl()));
            //TODO : wish or hits?
            totalWishes += product.getHits();
        }
        return new BrandDetailResponse(brand, totalWishes, dtos);
    }
}
