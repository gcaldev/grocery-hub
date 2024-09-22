package com.grocery_hub.grocery_hub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrefourProductDTO {

    private Long id;

    private String nombre;

    private String categoria;

    private Double precio;

    private Integer stock;

    private String imagen;
}

