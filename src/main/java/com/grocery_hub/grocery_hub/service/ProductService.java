package com.grocery_hub.grocery_hub.service;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getProducts(String searchName) throws Exception;
    boolean isGivenMarket(SuperMarketEnum marketType);
}
