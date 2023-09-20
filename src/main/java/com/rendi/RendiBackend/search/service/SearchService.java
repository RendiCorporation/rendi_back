package com.rendi.RendiBackend.search.service;

import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;

    @Transactional
    public List<Long> getSimilarProducts(String keywordName){
        List<Product> products = productRepository.findAll();
        List<Long> searchProductIds = new ArrayList<>();
        for (Product product : products){
            String productKeywords = product.getKeywords();
            log.info("double : {}", calculate(keywordName, productKeywords));
            if (productKeywords != null && calculate(keywordName, productKeywords) > 0.05){

                searchProductIds.add(product.getId());
            }
        }
        return searchProductIds;
    }
    public double calculate(String text1, String text2) {
        List<String> words1 = Arrays.asList(text1.split("\\s+"));
        List<String> words2 = null; // Initialize words2 as null

        if (text2 != null) {
            words2 = Arrays.asList(text2.split("\\s+"));
        }

        // Check if words1 and words2 are not null before proceeding
        if (words1 != null && words2 != null) {
            Set<String> allWords = new HashSet<>();
            allWords.addAll(words1);
            allWords.addAll(words2);

            int[] vector1 = createVector(allWords, words1);
            int[] vector2 = createVector(allWords, words2);

            // Rest of your code that uses vector1 and vector2

            if (vector1 != null && vector2 != null) {
                return dotProduct(vector1, vector2) / (magnitude(vector1) * magnitude(vector2));
            } else {
                // Handle the case where vector1 or vector2 is null
                // Possibly by assigning a default value or reporting an error
                return 0.0; // Change this to an appropriate default value or error handling
            }
        } else {
            // Handle the case where either words1 or words2 is null
            // Possibly by assigning a default value or reporting an error
            return 0.0; // Change this to an appropriate default value or error handling
        }
    }




    private int[] createVector(Set<String> allWords, List<String> wordList) {
        Map<String, Long> wordCount = wordList.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        return allWords.stream()
                .mapToInt(word -> wordCount.getOrDefault(word, 0L).intValue())
                .toArray();
    }

    private double dotProduct(int[] vectorA, int[] vectorB) {
        int sum = 0;

        for (int i=0; i<vectorA.length; i++) {
            sum += vectorA[i] * vectorB[i];
        }

        return sum;
    }

    private double magnitude(int[] vector) {
        double sumMag=0;

        for (int value : vector){
            sumMag += value*value;
        }

        return Math.sqrt(sumMag);
    }

//    @Transactional
//    public List<Long> searchByKeyword(String inputKeyword){
//        List<Long> productIds = new ArrayList<>();
//        productIds.add(1L);
//        return productIds;
//    }

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


