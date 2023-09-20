package com.rendi.RendiBackend.repositories;

import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.product.domain.Product;
import com.rendi.RendiBackend.wish.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByMemberAndProduct(Member member, Product product);
    void deleteByMemberAndProduct(Member member, Product product);
    List<Wish> findAllByMember(Member member);
}
