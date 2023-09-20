package com.rendi.RendiBackend.search.service;

import com.rendi.RendiBackend.repositories.PopularRepository;
import com.rendi.RendiBackend.search.domain.Popular;
import com.rendi.RendiBackend.search.dto.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularService {
    private final PopularRepository popularRepository;
    @Transactional
    public void incrementSearchCount(String keyword) {
        Optional<Popular> byKeyword = popularRepository.findByKeyword(keyword);

        if (byKeyword.isPresent()) {
            Popular existingKeyword = byKeyword.get();
            existingKeyword.setSearchCount(existingKeyword.getSearchCount() + 1);
        } else {
            Popular newKeyword = new Popular(keyword, 1L);
            popularRepository.save(newKeyword);
        }
    }
    @Transactional
    public List<PopularKeywordResponse> getPopularKeywords(){
        List<PopularKeywordResponse> dtos = new ArrayList<>();
        int topN = 10;
        Pageable pageable = PageRequest.of(0, topN, Sort.by(Sort.Direction.DESC, "searchCount"));
        List<Popular> topKeywords = popularRepository.findAll(pageable).getContent();
//        return topKeywords.stream()
//                .map(keyword -> new PopularKeywordResponse(keyword.getKeyword(), keyword.getSearchCount()))
//                .collect(Collectors.toList());
        for(Popular keyword : topKeywords){
            dtos.add(new PopularKeywordResponse(keyword.getKeyword(), keyword.getSearchCount()));
        }
        return dtos;
    }
}
