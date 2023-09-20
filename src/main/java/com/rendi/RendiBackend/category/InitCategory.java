package com.rendi.RendiBackend.category;

import com.rendi.RendiBackend.category.Category;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCategory {

    private final InitCategoryService initCategoryService;

    @PostConstruct
    public void init() {
        initCategoryService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCategoryService {

        private final EntityManager em;

        public void init() {
            Category category1 = createCategory(null, "상의", 1);
            Category category2 = createCategory(null, "아우터", 2);
            Category category3 = createCategory(null, "원피스/세트", 3);
            Category category4 = createCategory(null, "바지", 4);
            Category category5 = createCategory(null, "스커트", 5);
            Category category6 = createCategory(null, "트레이닝", 6);
            Category category7 = createCategory(null, "이너웨어", 7);
            Category category8 = createCategory(null, "수영복", 8);
            Category category9 = createCategory(null, "패션잡화", 9);
            Category category10 = createCategory(null, "가방", 10);
            Category category11 = createCategory(null, "기타", 11);

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);
            em.persist(category5);
            em.persist(category6);
            em.persist(category7);
            em.persist(category8);
            em.persist(category9);
            em.persist(category10);
            em.persist(category11);

            createCategory(category1, "전체", 1);
            createCategory(category1, "반소매 티셔츠", 2);
            createCategory(category1, "긴소매 티셔츠", 3);
            createCategory(category1, "블라우스", 4);
            createCategory(category1, "셔츠", 5);
            createCategory(category1, "민소매", 6);
            createCategory(category1, "니트", 7);
            createCategory(category1, "조끼", 8);
            createCategory(category1, "후드", 9);
            createCategory(category1, "맨투맨", 10);

            createCategory(category2, "전체", 1);
            createCategory(category2, "가디건", 2);
            createCategory(category2, "바람막이", 3);
            createCategory(category2, "자켓", 4);
            createCategory(category2, "코트", 5);
            createCategory(category2, "패딩", 6);
            createCategory(category2, "플리스", 7);
            createCategory(category2, "집업/점퍼", 8);
            createCategory(category2, "야상", 9);

            createCategory(category3, "전체", 1);
            createCategory(category3, "미니원피스", 2);
            createCategory(category3, "롱원피스", 3);
            createCategory(category3, "투피스", 4);
            createCategory(category3, "점프수트", 5);
            createCategory(category3, "미디원피스", 6);

            createCategory(category4, "전체", 1);
            createCategory(category4, "롱팬츠", 2);
            createCategory(category4, "숏팬츠", 3);
            createCategory(category4, "슬랙스", 4);
            createCategory(category4, "데님", 5);

            createCategory(category5, "전체", 1);
            createCategory(category5, "미니스커트", 2);
            createCategory(category5, "롱스커트", 3);
            createCategory(category5, "미디스커트", 4);

            createCategory(category6, "전체", 1);
            createCategory(category6, "트레이닝 하의", 2);
            createCategory(category6, "트레이닝 상의", 3);
            createCategory(category6, "트레이닝 세트", 4);
            createCategory(category6, "레깅스", 5);

            createCategory(category7, "전체", 1);
            createCategory(category7, "브라", 2);
            createCategory(category7, "팬티", 3);
            createCategory(category7, "속옷세트", 4);
            createCategory(category7, "이너", 5);
            createCategory(category7, "보정", 6);

            createCategory(category8, "전체", 1);
            createCategory(category8, "비키니", 2);
            createCategory(category8, "원피스수영복", 3);
            createCategory(category8, "모노키니", 4);
            createCategory(category8, "비치상의", 5);
            createCategory(category8, "비치하의", 6);
            createCategory(category8, "래쉬가드", 7);
            createCategory(category8, "악세사리", 8);
            createCategory(category8, "아쿠아슈즈", 9);

            createCategory(category9, "전체", 1);
            createCategory(category9, "플랫/로퍼", 2);
            createCategory(category9, "블로퍼/뮬", 3);
            createCategory(category9, "스니커즈", 4);
            createCategory(category9, "샌들", 5);
            createCategory(category9, "힐", 6);
            createCategory(category9, "워커/부츠", 7);
            createCategory(category9, "슬리퍼/쪼리", 8);
            createCategory(category9, "주얼리", 9);
//            createCategory(category9, "기타", 10);

            createCategory(category10, "전체", 1);
            createCategory(category10, "백팩", 2);
            createCategory(category10, "크로스백", 3);
            createCategory(category10, "숄더백", 4);
            createCategory(category10, "토트백", 5);
            createCategory(category10, "클러치", 6);
            createCategory(category10, "에코백", 7);
            createCategory(category10, "파우치", 8);
            createCategory(category10, "지갑", 9);
            createCategory(category10, "캐리어", 10);

            createCategory(category11, "전체", 1);
            createCategory(category11, "헤어", 2);
            createCategory(category11, "모자", 3);
            createCategory(category11, "아이웨어", 4);
            createCategory(category11, "머플러/스카프", 5);
            createCategory(category11, "장갑", 6);
            createCategory(category11, "벨트", 7);
            createCategory(category11, "양말/스타킹", 8);
            createCategory(category11, "시계", 9);
            createCategory(category11, "마스크", 10);
//            createCategory(category11, "기타", 11);



//            em.flush();

        }
        private Category createCategory(Category parent, String categoryName, int listOrder) {
            Category category = new Category(parent, categoryName, listOrder);
            if (parent != null) {
                parent.getChildren().add(category);
            }
            return category;
        }

    }


}