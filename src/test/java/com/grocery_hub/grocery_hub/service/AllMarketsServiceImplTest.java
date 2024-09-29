package com.grocery_hub.grocery_hub.service;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.impl.AllMarketsServiceImpl;
import com.grocery_hub.grocery_hub.service.impl.CarrefourServiceImpl;
import com.grocery_hub.grocery_hub.service.impl.DiaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllMarketsServiceImplTest {
    @InjectMocks
    private AllMarketsServiceImpl allMarketsService;

    private List<ProductService> productServices = new ArrayList<>();

    @Mock
    private CarrefourServiceImpl carrefourService;

    @Mock
    private DiaServiceImpl diaService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void givenNoProductSearchImpl_whenGetProducts_thenEmptyList() {
        List<ProductService> productServices = List.of(allMarketsService);
        ReflectionTestUtils.setField(allMarketsService, "productServices", productServices);

        List<ProductDTO> products = allMarketsService.getProducts("Fruta");

        assertThat(products).isEmpty();
    }

    @Test
    public void givenMultipleProductSearchIsEmpty_whenGetProducts_thenEmptyList() {
        when(carrefourService.getProducts(anyString())).thenReturn(List.of());
        when(carrefourService.isGivenMarket(any())).thenReturn(false);
        when(diaService.getProducts(anyString())).thenReturn(List.of());
        when(diaService.isGivenMarket(any())).thenReturn(false);
        List<ProductService> productServices = List.of(allMarketsService,carrefourService, diaService);
        ReflectionTestUtils.setField(allMarketsService, "productServices", productServices);

        List<ProductDTO> products = allMarketsService.getProducts("Fruta");

        assertThat(products).isEmpty();
    }

    @Test
    public void givenMultipleProductSearchIsNotEmpty_whenGetProducts_thenList() {
        List<ProductDTO> carrefourProducts = List.of(ProductDTO.builder().name("Banana").build());
        List<ProductDTO> diaProducts = List.of(ProductDTO.builder().name("Manzana").build());
        List<ProductDTO> expectedProducts = Stream.concat(carrefourProducts.stream(), diaProducts.stream()).toList();
        when(carrefourService.getProducts(anyString())).thenReturn(carrefourProducts);
        when(carrefourService.isGivenMarket(any())).thenReturn(false);
        when(diaService.getProducts(anyString())).thenReturn(diaProducts);
        when(diaService.isGivenMarket(any())).thenReturn(false);
        List<ProductService> productServices = List.of(allMarketsService,carrefourService, diaService);
        ReflectionTestUtils.setField(allMarketsService, "productServices", productServices);

        List<ProductDTO> products = allMarketsService.getProducts("Fruta");

        assertThat(products)
                .hasSize(2)
                .containsExactlyElementsOf(expectedProducts);
    }
}
