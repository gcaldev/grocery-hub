package com.grocery_hub.grocery_hub.service;

import com.grocery_hub.grocery_hub.TestingUtils;
import com.grocery_hub.grocery_hub.mapper.DiaProductMapperImpl;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.impl.DiaServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiaServiceImplTest {
    @InjectMocks
    private DiaServiceImpl diaService;

    @Mock
    private DiaProductMapperImpl diaProductMapper;

    private final TestingUtils testingUtils = new TestingUtils();

    /*
    3c4d5e6f7g8h9i0j1k2l3,Fruta Mixta,Marca C,Fruitas,1.75,250g,90,fruta_mixta.jpg,2025-03-10,Perú,Mezcla de frutas tropicales,4,200
4d5e6f7g8h9i0j1k2l3m4,Fruta Orgánica,Marca D,Fruitas,3.00,500g,150,fruta_organica.jpg,2025-04-25,Colombia,Frutas orgánicas sin pesticidas,2,80
     */

    private final ProductDTO productA = ProductDTO.builder()
            .id("3c4d5e6f7g8h9i0j1k2l3")
            .name("Fruta Mixta")
            .category("Fruitas")
            .price(1.75)
            .stock(200)
            .imageUrl("fruta_mixta.jpg")
            .provider("DIA")
            .build();

    private final ProductDTO productB = ProductDTO.builder()
            .id("4d5e6f7g8h9i0j1k2l3m4")
            .name("Fruta Orgánica")
            .category("Fruitas")
            .price(3.00)
            .stock(80)
            .imageUrl("fruta_organica.jpg")
            .provider("DIA")
            .build();

    @Test
    public void givenEmptyCsv_whenGetProducts_thenEmptyList() {
        ReflectionTestUtils.setField(diaService, "FTP_URL", "src/test/resources/mocks/DiaServiceNoProducts.csv");

        var products = diaService.getProducts("salmon");

        assertThat(products).isEmpty();
    }

    @Test
    public void givenCsvWithOneProduct_whenGetProducts_thenListWithOneProduct() throws URISyntaxException, IOException {
        ReflectionTestUtils.setField(diaService, "FTP_URL", "src/test/resources/mocks/DiaServiceOneProduct.csv");
        when(diaProductMapper.toProductDTO(anyString())).thenCallRealMethod();
        when(diaProductMapper.getProvider()).thenCallRealMethod();

        var products = diaService.getProducts("Fruta");

        assertThat(products)
                .hasSize(1)
                .containsExactlyElementsOf(List.of(productA));
    }

    @Test
    public void givenCsvWithMultipleProducts_whenGetProducts_thenListWithMultipleProducts() throws URISyntaxException, IOException {
        ReflectionTestUtils.setField(diaService, "FTP_URL", "src/test/resources/mocks/DiaServiceMultipleProducts.csv");
        when(diaProductMapper.toProductDTO(anyString())).thenCallRealMethod();
        when(diaProductMapper.getProvider()).thenCallRealMethod();

        var products = diaService.getProducts("Fruta");

        assertThat(products)
                .hasSize(2)
                .containsExactlyElementsOf(List.of(productA, productB));
    }

    @Test
    public void givenCsvFiltersNonMatchingProducts_whenGetProducts_thenListWithMatchingProducts() throws URISyntaxException, IOException {
        ReflectionTestUtils.setField(diaService, "FTP_URL", "src/test/resources/mocks/DiaServiceMultipleProducts.csv");
        when(diaProductMapper.toProductDTO(anyString())).thenCallRealMethod();
        when(diaProductMapper.getProvider()).thenCallRealMethod();

        var products = diaService.getProducts("Fruta");

        assertThat(products)
                .hasSize(2)
                .containsExactlyElementsOf(List.of(productA,productB));
    }
}
