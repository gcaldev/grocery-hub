package com.grocery_hub.grocery_hub.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery_hub.grocery_hub.TestingUtils;
import com.grocery_hub.grocery_hub.mapper.CarrefourProductMapper;
import com.grocery_hub.grocery_hub.mapper.CarrefourProductMapperImpl;
import com.grocery_hub.grocery_hub.model.CarrefourProductDTO;
import com.grocery_hub.grocery_hub.service.impl.CarrefourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarrefourServiceImplTest {
    private final TestingUtils testingUtils = new TestingUtils();

    @InjectMocks
    private CarrefourServiceImpl carrefourService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CarrefourProductMapperImpl carrefourProductMapper;

    static ObjectMapper getMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(carrefourService, "URL", "http://localhost:8080");
        ReflectionTestUtils.setField(carrefourService, "ENDPOINT", "/products");
    }

    @Test
    public void givenAQueryThatDontMatchProducts_whenGetProducts_thenEmptyList() {
        ArrayList<CarrefourProductDTO> emptyList = new ArrayList<>();
        var expectedUrl = "http://localhost:8080/products?nombre_like=FRUTA RARA";
        when(restTemplate.getForObject(eq(expectedUrl), eq(CarrefourProductDTO[].class))).thenReturn(new CarrefourProductDTO[0]);

        var products = carrefourService.getProducts("FRUTA RARA");

        assertThat(products).isEmpty();
    }

    @Test
    public void givenAQueryThatMatchOneProduct_whenGetProducts_thenListOfProducts() throws IOException, URISyntaxException {
        var expectedUrl = "http://localhost:8080/products?nombre_like=Sandia";
        var apiProducts = testingUtils.getConfiguracionFromFile("mocks/CarrefourServiceResponseOneElement.json", new TypeReference<CarrefourProductDTO[]>() {});
        when(restTemplate.getForObject(eq(expectedUrl), eq(CarrefourProductDTO[].class))).thenReturn(apiProducts);
        when(carrefourProductMapper.toProductDTO(any(CarrefourProductDTO.class))).thenCallRealMethod();
        when(carrefourProductMapper.toProductDTOs(any())).thenCallRealMethod();
        var expectedProducts = carrefourProductMapper.toProductDTOs(List.of(apiProducts));

        var products = carrefourService.getProducts("Sandia");

        assertEquals(1, products.size());
        assertThat(products).containsExactlyElementsOf(expectedProducts);
    }

    @Test
    public void givenAQueryThatMatchMultipleProducts_whenGetProducts_thenListOfProducts() throws IOException, URISyntaxException {
        var expectedUrl = "http://localhost:8080/products?nombre_like=al";
        var apiProducts = testingUtils.getConfiguracionFromFile("mocks/CarrefourServiceResponseMultipleElements.json", new TypeReference<CarrefourProductDTO[]>() {});
        when(restTemplate.getForObject(eq(expectedUrl), eq(CarrefourProductDTO[].class))).thenReturn(apiProducts);
        when(carrefourProductMapper.toProductDTO(any(CarrefourProductDTO.class))).thenCallRealMethod();
        when(carrefourProductMapper.toProductDTOs(any())).thenCallRealMethod();
        var expectedProducts = carrefourProductMapper.toProductDTOs(List.of(apiProducts));

        var products = carrefourService.getProducts("al");

        assertEquals(6, products.size());
        assertThat(products).containsExactlyElementsOf(expectedProducts);
    }
}
