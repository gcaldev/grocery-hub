package com.grocery_hub.grocery_hub.service.impl;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllMarketsServiceImpl implements ProductService {
    private final List<ProductService> productServices;

    @Autowired
    public AllMarketsServiceImpl(List<ProductService> productServices) {
        this.productServices = productServices;
    }

    @Override
    public boolean isGivenMarket(SuperMarketEnum marketType) {
        return SuperMarketEnum.ALL.equals(marketType);
    }

    private List<ProductDTO> getProductsHandler(ProductService productService, String searchName) {
        try {
            return productService.getProducts(searchName);
        } catch(Exception e) {
            System.out.println("Error fetching products from " + productService.getClass().getSimpleName());
            return List.of();
        }
    }

    @Override
    public List<ProductDTO> getProducts(String searchName) {
        return productServices
                .parallelStream()
                .filter(x -> !x.isGivenMarket(SuperMarketEnum.ALL))
                .map(x -> getProductsHandler(x, searchName))
                .flatMap(List::stream)
                .toList();
    }
}
