package com.grocery_hub.grocery_hub.model;
import lombok.Data;
import java.util.List;

@Data
public class EcommerceDTO {
    private String currencyCode;
    private List<LaAnonimaProductDTO> impressions;
}