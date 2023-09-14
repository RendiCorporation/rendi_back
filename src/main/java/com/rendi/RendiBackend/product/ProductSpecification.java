//package com.rendi.RendiBackend.product;
//
//import com.rendi.RendiBackend.product.domain.Product;
//import org.springframework.data.jpa.domain.Specification;
//
//public class ProductSpecification {
//
//    public static Specification<Product> hasParentCategory(String parentCategory) {
//        return (root, query, cb) -> {
//            if (parentCategory == null) {
//                return cb.isTrue(cb.literal(true)); // always true = no filtering
//            }
//            return cb.equal(root.get("parentCategory"), parentCategory);
//        };
//    }
//
//    public static Specification<Product> hasChildCategory(String childCategory) {
//        // similar to above...
//    }
//
//    public static Specification<Product> hasColour(String colour) {
//        // similar to above...
//    }
//
//    public static Specification<Product> inPriceRange(String priceRange) {
//        // You'll need to parse the price range into two values and then use cb.between()
//        // Or if it's just a single value, convert it into appropriate type and use cb.equal()
//    }
//}
