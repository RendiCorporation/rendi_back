package com.rendi.RendiBackend.product.service;

import com.rendi.RendiBackend.brand.domain.Brand;
import com.rendi.RendiBackend.brand.exception.BrandErrorCode;
import com.rendi.RendiBackend.brand.exception.BrandException;
import com.rendi.RendiBackend.category.Category;
import com.rendi.RendiBackend.repositories.CategoryRepository;
import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.service.MemberService;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.dto.*;
import com.rendi.RendiBackend.product.exception.ProductErrorCode;
import com.rendi.RendiBackend.product.exception.ProductException;
import com.rendi.RendiBackend.repositories.BrandRepository;
import com.rendi.RendiBackend.repositories.ProductRepository;
import com.rendi.RendiBackend.colour.Colour;
import com.rendi.RendiBackend.repositories.ColourRepository;
import com.rendi.RendiBackend.product.dto.SearchGuestResponse;
import com.rendi.RendiBackend.product.elastic.ProductSearchRepository;
import com.rendi.RendiBackend.search.service.SearchService;
import com.rendi.RendiBackend.wish.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ColourRepository colourRepository;
    private final CategoryRepository categoryRepository;
    private final WishService wishService;
    private final MemberService memberService;
    private final SearchService searchService;
    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public void autoCreateProducts(List<ProductSaveRequest> requestList){
        for (ProductSaveRequest request : requestList) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(()-> new BrandException(BrandErrorCode.BRAND_NOT_FOUND_BY_ID));
            log.info("product title duplicated : {}", request.getTitle());
            if(productRepository.findByTitle(request.getTitle()).isPresent()){
                throw new ProductException(ProductErrorCode.PRODUCT_TITLE_DUPLICATED);
            }
            Set<Colour> colours = new HashSet<>();
            for (String colourName : request.getColourNames()) {
                log.info("product title : {}", request.getTitle());
                Colour colour = colourRepository.findByColourName(colourName)
                        .orElseThrow(()-> new ProductException(ProductErrorCode.COLOUR_NOT_FOUND_BY_COLOUR_NAME));
                colours.add(colour);
            }
            Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            Product product = request.toProduct();
            product.setBrand(brand);
            product.setColours(colours);
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
        if (categoryName == null || categoryName.isEmpty()) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            Category parentCategory = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            List<Category> subCategories = parentCategory.getAllSubcategories();
            subCategories.add(parentCategory);
            products = productRepository.findByCategoryInOrderByCreatedAtDesc(subCategories);
        }
        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(),
                    product.getTitle(), product.getProductImgUrl(), product.getDetailUrl()));

            count++;
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getNewProductsUser(String categoryName) {
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            Category parentCategory = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            List<Category> subCategories = parentCategory.getAllSubcategories();
            subCategories.add(parentCategory);
            products = productRepository.findByCategoryInOrderByCreatedAtDesc(subCategories);
        }
        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    wishYN, product.getProductImgUrl(), product.getDetailUrl()));
            count++;
        }
        return dtos;
    }

//    @Transactional
//    public Page<ProductUserResponse> getNewProductsUser(String categoryName, Pageable pageable) {
//        Member member = memberService.findCurrentMember();
//        Page<Product> products;
//
//        // Create a new PageRequest object with the sorting applied
//        Pageable sortedByCreatedAtDesc =
//                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
//
//        if (categoryName == null || categoryName.isEmpty()) {
//            products = productRepository.findAll(sortedByCreatedAtDesc);
//        } else {
//            Category parentCategory = categoryRepository.findByCategoryName(categoryName)
//                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
//            List<Category> subCategories = parentCategory.getAllSubcategories();
//            subCategories.add(parentCategory);
//
//            products = productRepository.findByCategoryIn(subCategories, sortedByCreatedAtDesc);
//        }
//
//        return products.map(product -> {
//            boolean wishYN = wishService.checkWishes(member, product);
//            return new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(),
//                    product.getTitle(), wishYN, product.getProductImgUrl(),
//                    product.getDetailUrl());
//        });
//    }



    @Transactional
    public List<ProductGuestResponse> getBestProductsGuest(String categoryName){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products;
        if (categoryName == null || categoryName.isEmpty()) {
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "hits"));
        } else {
            Category parentCategory = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            List<Category> subCategories = parentCategory.getAllSubcategories();
            subCategories.add(parentCategory);
            products = productRepository.findByCategoryInOrderByHitsDesc(subCategories);
        }
        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(),
                    product.getTitle(), product.getProductImgUrl(), product.getDetailUrl()));

            count++;
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
            Category parentCategory = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            List<Category> subCategories = parentCategory.getAllSubcategories();
            subCategories.add(parentCategory);
            products = productRepository.findByCategoryInOrderByHitsDesc(subCategories);
        }
        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    wishYN, product.getProductImgUrl(), product.getDetailUrl()));
            count++;
        }
        return dtos;
    }
    @Transactional
    public List<ProductUserResponse> getProductsByCategoryUser(String parentName, String childName){
        List<ProductUserResponse> dtos = new ArrayList<>();
        Member member = memberService.findCurrentMember();
        List<Product> products = new ArrayList<>();
        if ((parentName == null || parentName.isEmpty()) && (childName == null || childName.isEmpty())) {
            products.addAll(productRepository.findAll());
        } else if (childName == null || childName.isEmpty()) {
            Category category = categoryRepository.findByCategoryName(parentName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));

            List<Category> allSubCategories = category.getAllSubcategories();
            allSubCategories.add(category);

            for (Category cat : allSubCategories) {
                products.addAll(productRepository.findByCategory(cat));
            }
        } else {
            Category category = categoryRepository.findByCategoryName(childName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products.addAll(productRepository.findByCategory(category));
        }
        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            boolean wishYN = wishService.checkWishes(member, product);
            dtos.add(new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                    wishYN, product.getProductImgUrl(), product.getDetailUrl()));
            count++;
        }
        return dtos;
    }
    @Transactional
    public List<ProductGuestResponse> getProductsByCategoryGuest(String parentName, String childName){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        if ((parentName == null || parentName.isEmpty()) && (childName == null || childName.isEmpty())) {
            products.addAll(productRepository.findAll());
        } else if (childName == null || childName.isEmpty()) {
            Category category = categoryRepository.findByCategoryName(parentName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));

            List<Category> allSubCategories = category.getAllSubcategories();
            allSubCategories.add(category);

            for (Category cat : allSubCategories) {
                products.addAll(productRepository.findByCategory(cat));
            }
        } else {
            Category category = categoryRepository.findByCategoryName(childName)
                    .orElseThrow(()->new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            products.addAll(productRepository.findByCategory(category));
        }

        int count = 0;
        for (Product product : products) {
            if (count >= 240) {
                break;
            }
            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(),
                    product.getTitle(), product.getProductImgUrl(), product.getDetailUrl()));

            count++;
        }

        return dtos;
    }


    @Transactional
    public List<ProductUserResponse> getAllProductsUser(String sortName, String parentCategory, String childCategory,
                                                        String colourName, Long minPrice, Long maxPrice){
        List<Product> products = productRepository.findAll();

        if ((sortName == null || sortName.isEmpty()) &&
                (parentCategory == null || parentCategory.isEmpty()) &&
                (childCategory == null || childCategory.isEmpty()) &&
                (colourName == null || colourName.isEmpty()) &&
                minPrice == null && maxPrice == null) {
            return products.stream().map(this::convertToDtoUser).collect(Collectors.toList());
        }

        Stream<Product> filteredProducts = products.stream();

        if (childCategory != null && !childCategory.isEmpty()){
            Category childCat= categoryRepository.findByCategoryName(childCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + childCategory));
            filteredProducts = filteredProducts.filter(product -> product.getCategory().equals(childCat));
        }

        if (parentCategory != null && !parentCategory.isEmpty()){
            Category parentCat= categoryRepository.findByCategoryName(parentCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + parentCategory));
            Set<Category> subcategories= new HashSet<>(parentCat.getAllSubcategories());
            filteredProducts = filteredProducts.filter(product -> subcategories.contains(product.getCategory()));
        }

        if (minPrice != null && maxPrice != null) {
            filteredProducts = filteredProducts.filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice);
        }

        if (colourName != null && !colourName.isEmpty()) {
            filteredProducts = filteredProducts.filter(product -> hasColour(product.getColours(), colourName));
        }

        List<Product> finalProductList;

        if (sortName != null && !sortName.isEmpty()){
            switch(sortName){
                case "추천순":
                    finalProductList = filteredProducts.collect(Collectors.toList());
                    Collections.shuffle(finalProductList);
                    break;
                case "인기순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getHits).reversed()).collect(Collectors.toList());
                    break;
                case "낮은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                    break;
                case "높은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
                    break;
                default:
                    finalProductList =  filteredProducts.collect(Collectors.toList());
            }
        } else {
            finalProductList =  filteredProducts.collect(Collectors.toList());
        }

//        return finalProductList.stream().map(this::convertToDtoUser).collect(Collectors.toList());
        return finalProductList.stream()
                .map(this::convertToDtoUser)
                .limit(240)
                .collect(Collectors.toList());


    }
    @Transactional
    public List<ProductGuestResponse> getAllProductsGuest(String sortName, String parentCategory, String childCategory,
                                                          String colourName, Long minPrice, Long maxPrice){
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        if ((sortName == null || sortName.isEmpty()) &&
                (parentCategory == null || parentCategory.isEmpty()) &&
                (childCategory == null || childCategory.isEmpty()) &&
                (colourName == null || colourName.isEmpty()) &&
                minPrice == null && maxPrice == null) {
            return products.stream().map(this::convertToDto).collect(Collectors.toList());
        }

        Stream<Product> filteredProducts = products.stream();

        if (childCategory != null && !childCategory.isEmpty()){
            Category childCat= categoryRepository.findByCategoryName(childCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + childCategory));
            filteredProducts = filteredProducts.filter(product -> product.getCategory().equals(childCat));
        }

        if (parentCategory != null && !parentCategory.isEmpty()){
            Category parentCat= categoryRepository.findByCategoryName(parentCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + parentCategory));
            Set<Category> subcategories= new HashSet<>(parentCat.getAllSubcategories());
            filteredProducts = filteredProducts.filter(product -> subcategories.contains(product.getCategory()));
        }

        if (minPrice != null && maxPrice != null) {
            filteredProducts = filteredProducts.filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice);
        }

        if (colourName != null && !colourName.isEmpty()) {
            filteredProducts = filteredProducts.filter(product -> hasColour(product.getColours(), colourName));
        }

        List<Product> finalProductList;

        if (sortName != null && !sortName.isEmpty()){
            switch(sortName){
                case "추천순":
                    finalProductList = filteredProducts.collect(Collectors.toList());
                    Collections.shuffle(finalProductList);
                    break;
                case "인기순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getHits).reversed()).collect(Collectors.toList());
                    break;
                case "낮은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                    break;
                case "높은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
                    break;
                default:
                    finalProductList =  filteredProducts.collect(Collectors.toList());
            }
        } else {
            finalProductList =  filteredProducts.collect(Collectors.toList());
        }

        return finalProductList.stream().map(this::convertToDto).limit(240).collect(Collectors.toList());

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

//    public List<ProductGuestResponse> searchProductAndSort(String sortName, String parentCategory, String childCategory,
//                                                           String colourName, Long minPrice, Long maxPrice) {
//        List<Long> productIds = Arrays.asList(1L, 2L, 4L);
//        List<Product> products = productRepository.findByIdIn(productIds);
//
//        return products.stream()
//                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
//                .filter(product -> hasColour(product.getColours(), colourName))
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//    @Transactional
//    public List<SearchGuestResponse> searchByKeywordGuest(String keywordName){
//        List<SearchGuestResponse> searchResponse = new ArrayList<>();
//        List<ProductGuestResponse> dtos = new ArrayList<>();
////        List<Long> productIds = new ArrayList<>();
//        List<Long> productIds = searchService.getSimilarProducts(keywordName);
//        List<Product> products = productRepository.findByIdIn(productIds);
////        List<Product> products = productSearchRepository.findByKeywordsFuzzyAndPartial(keywordName);
//        for (Product product : products) {
////            productIds.add(product.getId());
//            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
//                    ,product.getProductImgUrl(), product.getDetailUrl()));
//        }
//        searchResponse.add(new SearchGuestResponse(productIds, dtos));
//        return searchResponse;
//
//    }

//    public List<SearchGuestResponse> searchByKeywordGuest(String keywordName){
//        List<SearchGuestResponse> searchResponse = new ArrayList<>();
//        List<ProductGuestResponse> dtos = new ArrayList<>();
//        List<String> titles = springToFlask(keywordName);
//        List<Long> productIds = new ArrayList<>();
//
//        List<Product> products = productRepository.findByTitleIn(titles);
//
//        for (Product product : products) {
//            productIds.add(product.getId());
//            dtos.add(new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
//                    ,product.getProductImgUrl(), product.getDetailUrl()));
//        }
//        searchResponse.add(new SearchGuestResponse(productIds, dtos));
//        return searchResponse;
//    }
    @Transactional
    public List<SearchGuestResponse> searchByKeywordGuest(String keywordName){
        List<SearchGuestResponse> searchResponse = new ArrayList<>();
        List<ProductGuestResponse> dtos = new ArrayList<>();
        List<String> titles = springToFlask(keywordName);
        List<Long> productIds = new ArrayList<>();

        for (String title : titles) {
            Optional<Product> productOpt = productRepository.findByTitle(title);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                productIds.add(product.getId());
                dtos.add(new ProductGuestResponse(product.getId(),
                        product.getPrice(),
                        product.getBrand().getId(),
                        product.getTitle(),
                        product.getProductImgUrl(),
                        product.getDetailUrl()));
            }
        }

        searchResponse.add(new SearchGuestResponse(productIds, dtos));

        return searchResponse;
    }



    @Transactional
    public List<String> springToFlask(String keywordName){
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("name", keywordName);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity,
                new ParameterizedTypeReference<List<String>>() {});

        List<String> titles = responseEntity.getBody();
        log.info("title list = {}", titles);

        return titles;
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
    public List<ProductGuestResponse> searchProductAndSort(String sortName, String parentCategory, String childCategory,
                                                           String colourName, Long minPrice, Long maxPrice) {

        List<Long> productIds = LongStream.rangeClosed(1L, 100L)
                .boxed()
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByIdIn(productIds);
        if ((sortName == null || sortName.isEmpty()) &&
                (parentCategory == null || parentCategory.isEmpty()) &&
                (childCategory == null || childCategory.isEmpty()) &&
                (colourName == null || colourName.isEmpty()) &&
                minPrice == null && maxPrice == null) {
            return products.stream().map(this::convertToDto).collect(Collectors.toList());
        }

        Stream<Product> filteredProducts = products.stream();

        if (childCategory != null && !childCategory.isEmpty()){
            Category childCat= categoryRepository.findByCategoryName(childCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + childCategory));
            filteredProducts = filteredProducts.filter(product -> product.getCategory().equals(childCat));
        }

        if (parentCategory != null && !parentCategory.isEmpty()){
            Category parentCat= categoryRepository.findByCategoryName(parentCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + parentCategory));
            Set<Category> subcategories= new HashSet<>(parentCat.getAllSubcategories());
            filteredProducts = filteredProducts.filter(product -> subcategories.contains(product.getCategory()));
        }

        if (minPrice != null && maxPrice != null) {
            filteredProducts = filteredProducts.filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice);
        }

        if (colourName != null && !colourName.isEmpty()) {
            filteredProducts = filteredProducts.filter(product -> hasColour(product.getColours(), colourName));
        }

        List<Product> finalProductList;

        if (sortName != null && !sortName.isEmpty()){
            switch(sortName){
                case "추천순":
                    finalProductList = filteredProducts.collect(Collectors.toList());
                    Collections.shuffle(finalProductList);
                    break;
                case "인기순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getHits).reversed()).collect(Collectors.toList());
                    break;
                case "낮은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                    break;
                case "높은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
                    break;
                default:
                    finalProductList =  filteredProducts.collect(Collectors.toList());
            }
        } else {
            finalProductList =  filteredProducts.collect(Collectors.toList());
        }

        return finalProductList.stream().map(this::convertToDto).limit(240).collect(Collectors.toList());


    }

    private boolean hasColour(Set<Colour> colours, String colourName) {
        return colours.stream().anyMatch(colour -> colour.getColourName().equals(colourName));
    }
    private ProductGuestResponse convertToDto(Product product) {
        return new ProductGuestResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle()
                ,product.getProductImgUrl(), product.getDetailUrl()
        );
    }
    private ProductUserResponse convertToDtoUser(Product product) {
        Member member = memberService.findCurrentMember();
        boolean wishYN = wishService.checkWishes(member, product);
        return new ProductUserResponse(product.getId(), product.getPrice(), product.getBrand().getId(), product.getTitle(),
                wishYN, product.getProductImgUrl(), product.getDetailUrl()
        );
    }

    public List<ProductGuestResponse> searchProductsFilter(List<Long> productIds, String sortName, String parentCategory, String childCategory,
                                                           String colourName, Long minPrice, Long maxPrice) {
        List<Product> products = productRepository.findByIdIn(productIds);
        if ((sortName == null || sortName.isEmpty()) &&
                (parentCategory == null || parentCategory.isEmpty()) &&
                (childCategory == null || childCategory.isEmpty()) &&
                (colourName == null || colourName.isEmpty()) &&
                minPrice == null && maxPrice == null) {
            return products.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        Stream<Product> filteredProducts = products.stream();
        if (childCategory != null && !childCategory.isEmpty()){
            Category childCat= categoryRepository.findByCategoryName(childCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + childCategory));
            filteredProducts = filteredProducts.filter(product -> product.getCategory().equals(childCat));
        }
        if (parentCategory != null && !parentCategory.isEmpty()){
            Category parentCat= categoryRepository.findByCategoryName(parentCategory)
                    .orElseThrow(() -> new IllegalArgumentException("No category found with name: " + parentCategory));
            Set<Category> subcategories= new HashSet<>(parentCat.getAllSubcategories());
            filteredProducts = filteredProducts.filter(product -> subcategories.contains(product.getCategory()));
        }

        if (minPrice != null && maxPrice != null) {
            filteredProducts = filteredProducts.filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice);
        }

        if (colourName != null && !colourName.isEmpty()) {
            filteredProducts = filteredProducts.filter(product -> hasColour(product.getColours(), colourName));
        }

        List<Product> finalProductList;

        if (sortName != null && !sortName.isEmpty()){
            switch(sortName){
                case "추천순":
                    finalProductList = filteredProducts.collect(Collectors.toList());
                    Collections.shuffle(finalProductList);
                    break;
                case "인기순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getHits).reversed()).collect(Collectors.toList());
                    break;
                case "낮은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                    break;
                case "높은가격순":
                    finalProductList = filteredProducts.sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
                    break;
                default:
                    finalProductList =  filteredProducts.collect(Collectors.toList());
            }
        } else {
            finalProductList =  filteredProducts.collect(Collectors.toList());
        }

        return finalProductList.stream().map(this::convertToDto).limit(240).collect(Collectors.toList());


    }
    public List<Product> fuzzySearchProductsByKeywords(String keywordName) {
        return productSearchRepository.findByKeywordsFuzzyAndPartial(keywordName);
    }
//
//    public List<Product> sortedByFilter(String sortName, String parentCategory, String childCategory,
//                                        String colourName, String priceTag) {
//        if (parentCategory != null){
//            Category category = categoryRepository.findByCategoryName(parentCategory)
//                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
//            return productRepository.findByCategory(category);
//        }
//        if (childCategory != null){
//            Category category = categoryRepository.findByCategoryName(childCategory)
//                    .orElseThrow(()-> new ProductException(ProductErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
//            return productRepository.findByCategory(category);
//        }
//        if (colourName != null){
//            Colour colour = colourRepository.findByColourName(colourName)
//                    .orElseThrow(()->new ProductException(ProductErrorCode.COLOUR_NOT_FOUND_BY_COLOUR_NAME));
//            return productRepository.findByColour(colour);
//        }
//
//
//        return productRepository.findAll();
//    }
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
