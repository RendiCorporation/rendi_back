package com.rendi.RendiBackend.search.service;

import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.service.MemberService;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.dto.ProductUserResponse;
import com.rendi.RendiBackend.product.repository.ProductRepository;
import com.rendi.RendiBackend.wish.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final WishService wishService;
    private final MemberService memberService;
    private final ProductRepository productRepository;
    @Transactional
    public List<ProductUserResponse> searchByKeyword(String keywordName){
        List<ProductUserResponse> dtos = new ArrayList<>();
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
    public List<ProductUserResponse> searchByImage(String imgUrl){
        List<ProductUserResponse> dtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        Member member = memberService.findCurrentMember();
        for (Product product : products) {
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                    ,wishYN,product.getProductImgUrl(), product.getDetailUrl()));
        }
        return dtos;
    }

//    public List<Keyword> searchKeywords(String inputKeyword) {
//        // 입력된 키워드 벡터 생성
//        RealVector inputVector = createVectorFromKeyword(inputKeyword);
//
//        // 저장된 키워드 및 벡터 가져오기
//        List<Keyword> keywords = keywordRepository.findAll();
//
//        // 코사인 유사도 계산 및 저장된 키워드와 함께 저장
//        List<KeywordSimilarity> keywordSimilarities = new ArrayList<>();
//        for (Keyword keyword : keywords) {
//            RealVector keywordVector = createVectorFromKeyword(keyword.getKeywordList());
//            double cosineSimilarity = calculateCosineSimilarity(inputVector, keywordVector);
//            keywordSimilarities.add(new KeywordSimilarity(keyword, cosineSimilarity));
//        }
//
//        // 유사도가 높은 순서로 정렬
//        Collections.sort(keywordSimilarities, Collections.reverseOrder());
//
//        // 정렬된 결과를 키워드 리스트로 반환
//        List<Keyword> sortedKeywords = new ArrayList<>();
//        for (KeywordSimilarity keywordSimilarity : keywordSimilarities) {
//            sortedKeywords.add(keywordSimilarity.getKeyword());
//        }
//
//        return sortedKeywords;
//    }
//
//    private RealVector createVectorFromKeyword(String keyword) {
//        // 키워드를 벡터로 변환하는 로직을 구현하세요.
//        // 예: TF-IDF를 사용한 벡터화 등
//        // 아래는 간단한 더미 벡터 예시입니다.
//        double[] values = new double[keyword.length()];
//        for (int i = 0; i < keyword.length(); i++) {
//            values[i] = keyword.charAt(i);
//        }
//        return new ArrayRealVector(values);
//    }
//
//    private double calculateCosineSimilarity(RealVector vector1, RealVector vector2) {
//        // 코사인 유사도 계산 로직
//        return vector1.dotProduct(vector2) / (vector1.getNorm() * vector2.getNorm());
//    }
}


