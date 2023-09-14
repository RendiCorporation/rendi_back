//package com.rendi.RendiBackend.search;
//
//public class KeywordSimilarity implements Comparable<KeywordSimilarity> {
//    private Keyword keyword;
//    private double similarity;
//
//    public KeywordSimilarity(Keyword keyword, double similarity) {
//        this.keyword = keyword;
//        this.similarity = similarity;
//    }
//
//    public Keyword getKeyword() {
//        return keyword;
//    }
//
//    public double getSimilarity() {
//        return similarity;
//    }
//
//    @Override
//    public int compareTo(KeywordSimilarity other) {
//        // 유사도를 기준으로 정렬
//        return Double.compare(this.similarity, other.similarity);
//    }
//}