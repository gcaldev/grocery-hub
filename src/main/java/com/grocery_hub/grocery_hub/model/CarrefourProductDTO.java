package com.grocery_hub.grocery_hub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarrefourProductDTO {

    private Long id;

    private String nombre;

    private String categoria;

    private Double precio;

    private Integer stock;

    private String imagen;
}

