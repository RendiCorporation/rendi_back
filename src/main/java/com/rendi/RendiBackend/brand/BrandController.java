package com.rendi.RendiBackend.brand;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.brand.dto.BrandDetailResponse;
import com.rendi.RendiBackend.brand.dto.BrandListResponse;
import com.rendi.RendiBackend.brand.dto.BrandSaveRequest;
import com.rendi.RendiBackend.brand.service.BrandService;
import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.product.dto.ProductSaveRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;
    @PostMapping("add/hj/one")
    public StringResponse createBrand(@RequestBody BrandSaveRequest request) {
        Brand brand = brandService.createBrand(request);
        Long id = brand.getId();
        String name = brand.getBrandName();
        return new StringResponse("added new Brand. id: " + id + "brand name : " + name);
    }
    @PostMapping("add/hj")
    public StringResponse autoCreateBrands(@RequestBody List<BrandSaveRequest> requestList){
        brandService.autoCreateBrands(requestList);
        return new StringResponse("브랜드 리스트 DB 저장 완료");
    }
    @GetMapping("/all")
    public List<BrandListResponse> getAllBrands(){
        return brandService.getAllBrands();

    }
    @GetMapping("/details")
    public BrandDetailResponse findByBrandName(@RequestParam String brandName) {
        return brandService.getBrandDetails(brandName);
    }
}