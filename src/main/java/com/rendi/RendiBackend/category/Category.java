package com.rendi.RendiBackend.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    private String categoryName;

    private int listOrder;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Category> children = new ArrayList<>();

    public Category(Category parent, String categoryName, int listOrder) {
        this.parent = parent;
        this.categoryName = categoryName;
        this.listOrder = listOrder;
    }
    public List<Category> getAllSubcategories() {
        List<Category> subcategories = new ArrayList<>();
        for (Category child : children) {
            subcategories.add(child);
            subcategories.addAll(child.getAllSubcategories());
        }
        return subcategories;
    }
}
