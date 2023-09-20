package com.rendi.RendiBackend.product.elastic;

import com.rendi.RendiBackend.product.domain.Product;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<Product, String> {

    @Query("{\"bool\": {\"should\": [{\"match\": {\"keywords\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}, {\"match_phrase_prefix\": {\"keywords\": \"?0\"}}]}}")
    List<Product> findByKeywordsFuzzyAndPartial(String keywords);

//    @Query("{\"match\": {\"keywords\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
//    List<Product> findByKeywordsFuzzy(String keywords);
}
