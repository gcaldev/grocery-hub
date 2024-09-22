package com.grocery_hub.grocery_hub.service.impl;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.mapper.CarrefourProductMapper;
import com.grocery_hub.grocery_hub.model.CarrefourProductDTO;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CarrefourServiceImpl implements ProductService {
    @Value("${carrefour.url}")
    private String URL;

    @Value("${carrefour.endpoint.search}")
    private String ENDPOINT;

    private final CarrefourProductMapper carrefourProductMapper;

    @Autowired
    public CarrefourServiceImpl(CarrefourProductMapper carrefourProductMapper) {
        this.carrefourProductMapper = carrefourProductMapper;
    }

    @Override
    public boolean isGivenMarket(SuperMarketEnum marketType) {
        return SuperMarketEnum.CARREFOUR.equals(marketType);
    }

    private String getUri(String productName) {
        return UriComponentsBuilder.fromHttpUrl(this.URL).path(ENDPOINT)
                .queryParam("nombre_like", productName)
                .build().toUriString();
    }

    private List<CarrefourProductDTO> fetchProducts(String productName) {
        try {
            Thread.sleep(5000);

            String url = getUri(productName);
            RestTemplate restTemplate = new RestTemplate();

            CarrefourProductDTO[] products = restTemplate
                    .getForObject(url, CarrefourProductDTO[].class);

            return products != null ? Arrays.asList(products) : Collections.emptyList();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new RuntimeException("Error fetching products from Carrefour");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductDTO> getProducts(String searchName) {
        List<CarrefourProductDTO> carrefourProducts = fetchProducts(searchName);

        return carrefourProductMapper.toProductDTOs(carrefourProducts);
    }
}
