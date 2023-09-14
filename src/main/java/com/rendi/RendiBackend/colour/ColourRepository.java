package com.rendi.RendiBackend.colour;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColourRepository extends JpaRepository<Colour, Long> {
    Optional<Colour> findByColourName(String colourName);

}