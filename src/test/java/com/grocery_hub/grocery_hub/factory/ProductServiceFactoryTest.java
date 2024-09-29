package com.grocery_hub.grocery_hub.factory;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import com.grocery_hub.grocery_hub.service.impl.CarrefourServiceImpl;
import com.grocery_hub.grocery_hub.service.impl.DiaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceFactoryTest {
    @InjectMocks
    private ProductServiceFactory ProductServiceFactory;

    @Mock
    private CarrefourServiceImpl carrefourService;

    @Mock
    private DiaServiceImpl diaService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void givenNoMatchingImpl_whenGetProductService_thenThrowsException() {
        List<ProductService> productServices = List.of();
        ReflectionTestUtils.setField(ProductServiceFactory, "productServices", productServices);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ProductServiceFactory.getProductService(SuperMarketEnum.ALL));
        assertThat(exception).hasMessage("No such market found");
    }

    @Test
    public void givenMatchingImpl_whenGetProductService_thenReturnsService() {
        List<ProductService> productServices = List.of(carrefourService, diaService);
        ReflectionTestUtils.setField(ProductServiceFactory, "productServices", productServices);
        when(carrefourService.isGivenMarket(eq(SuperMarketEnum.CARREFOUR))).thenReturn(true);

        ProductService productService = ProductServiceFactory.getProductService(SuperMarketEnum.CARREFOUR);

        assertThat(productService).isInstanceOf(CarrefourServiceImpl.class);
    }
}
