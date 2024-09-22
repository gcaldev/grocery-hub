package com.grocery_hub.grocery_hub.factory;


import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceFactory {
    private final List<ProductService> productServices;

    @Autowired
    public ProductServiceFactory(List<ProductService> productServices) {
        this.productServices = productServices;
    }

    public ProductService getProductService(SuperMarketEnum marketType) {
        return productServices.stream()
                .filter(productService -> productService.isGivenMarket(marketType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such market found"));
    }
}
