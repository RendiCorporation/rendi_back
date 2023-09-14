package com.rendi.RendiBackend.wish;

import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.service.MemberService;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.product.repository.ProductRepository;
import com.rendi.RendiBackend.wish.domain.Wish;
import com.rendi.RendiBackend.wish.dto.WishListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

//    @Transactional
//    public Wish saveWishes(WishSaveRequest request){
//        Member member = memberService.findCurrentMember();
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID));
//        return wishRepository.save(new Wish(member, product));
//    }

    @Transactional
    public boolean inWishListTF(Product product){
        Member member = memberService.findCurrentMember();
        if(checkWishes(member, product)){
            /* 해당 상품을 찜했다면 찜 목록에서 삭제 */
            deleteWishes(member, product);
            return true;
        } else {
            /* 해당 상품을 찜하지 않았다면 찜 목록에 강의 추가 */
            saveWishes(member, product);
            return false;
        }
    }

    @Transactional
    public List<WishListResponse> readWishes(){
        Member member = memberService.findCurrentMember();
        List<WishListResponse> dtos = new ArrayList<>();
        List<Wish> wishes = wishRepository.findAllByMember(member);
        for(Wish wish : wishes){
            dtos.add(new WishListResponse(wish.getProduct().getId(), wish.getProduct().getPrice(), wish.getProduct().getBrand().getId(), wish.getProduct().getTitle()));
        }
        return dtos;
    }

    public void saveWishes(Member member, Product product){
        wishRepository.save(new Wish(member, product));
    }

    public boolean checkWishes(Member member, Product product) {
        return wishRepository.existsByMemberAndProduct(member, product);
    }

    public void deleteWishes(Member member, Product product) {
        wishRepository.deleteByMemberAndProduct(member, product);
    }


}
