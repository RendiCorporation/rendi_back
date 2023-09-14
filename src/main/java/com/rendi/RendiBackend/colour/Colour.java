package com.rendi.RendiBackend.colour;

import com.rendi.RendiBackend.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Colour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colour_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String colourName;

    @ManyToMany(mappedBy="colours")
    Set<Product> products;

    public Colour(String colourName) {
        this.colourName = colourName;
    }
}
