package com.rendi.RendiBackend.search;

import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.product.service.ProductService;
import com.rendi.RendiBackend.search.dto.PopularKeywordResponse;
import com.rendi.RendiBackend.search.dto.SearchKeywordRequest;
import com.rendi.RendiBackend.search.service.PopularService;
import com.rendi.RendiBackend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final PopularService popularService;
    private final ProductService productService;
    private final SearchService searchService;

    @PostMapping("/keyword/update")
    public StringResponse createKeyword(@RequestBody SearchKeywordRequest request){
        popularService.incrementSearchCount(request.getKeyword());
        return new StringResponse("검색어 DB 업데이트 성공");
    }
    @GetMapping("/keyword/popular")
    public List<PopularKeywordResponse> getPopular10(){
        return popularService.getPopularKeywords();
    }



//    @GetMapping("/guest/filter")
//    public List<ProductGuestResponse> SortedByFilterGuest(@RequestParam(required = true) List<Long> productIds,
//                                                     @RequestParam(required = false) String sortName,
//                                                     @RequestParam(required = false) String parentCategory,
//                                                     @RequestParam(required = false) String childCategory,
//                                                     @RequestParam(required = false) String colourName,
//                                                     @RequestParam(required = false) Long minPrice,
//                                                     @RequestParam(required = false) Long maxPrice){
//
//        return productService.searchProductAndSortGuest(productIds, sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
//    }
//    @GetMapping("/filter")
//    public List<ProductUserResponse> SortedByFilterUser(@RequestParam(required = true) List<Long> productIds,
//                                                        @RequestParam(required = false) String sortName,
//                                                     @RequestParam(required = false) String parentCategory,
//                                                     @RequestParam(required = false) String childCategory,
//                                                     @RequestParam(required = false) String colourName,
//                                                     @RequestParam(required = false) Long minPrice,
//                                                     @RequestParam(required = false) Long maxPrice){
//
//        return productService.searchProductAndSortUser(productIds, sortName, parentCategory, childCategory, colourName, minPrice, maxPrice);
//    }

}
