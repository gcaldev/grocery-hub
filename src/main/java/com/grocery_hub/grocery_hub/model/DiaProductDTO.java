package com.grocery_hub.grocery_hub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"id", "nombre", "marca", "categoria", "precio", "peso", "calorias", "imagen", "fecha_expiracion", "origen", "descripcion", "unidades_por_paquete", "stock"})
public class DiaProductDTO {
    private String id;

    private String nombre;

    private String marca;

    private String categoria;

    private Double precio;

    private String peso;

    private Integer calorias;

    private String imagen;

    @JsonProperty("fecha_expiracion")
    private String fechaExpiracion;

    private String origen;

    private String descripcion;

    @JsonProperty("unidades_por_paquete")
    private Integer unidadesPorPaquete;

    private Integer stock;
}
