package com.rendi.RendiBackend.colour;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
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
    public Colour(String colourName) {
        this.colourName = colourName;
    }

}