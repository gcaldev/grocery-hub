package com.grocery_hub.grocery_hub.constants;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible supermarket values")
public enum SuperMarketEnum {

    @Schema(description = "All supermarkets")
    ALL("ALL"),

    @Schema(description = "La Anonima supermarket")
    LA_ANONIMA("LA_ANONIMA"),

    @Schema(description = "DIA supermarket")
    DIA("DIA"),

    @Schema(description = "Carrefour supermarket")
    CARREFOUR("CARREFOUR");

    private final String value;

    SuperMarketEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
