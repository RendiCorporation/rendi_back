package com.rendi.RendiBackend.search;

import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.product.dto.ProductUserResponse;
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

    @GetMapping("/keyword")
    public List<ProductUserResponse> getProductsByKeyword(@RequestParam String keywordName){
        return searchService.searchByKeyword(keywordName);
    }
    @GetMapping("/image")
    public List<ProductUserResponse> getProductsByImage(@RequestParam String imgUrl){
        return searchService.searchByImage(imgUrl);
    }
}
