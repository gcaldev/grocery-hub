package com.grocery_hub.grocery_hub.mapper;
import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.CarrefourProductDTO;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarrefourProductMapper {
    @Mapping(target = "name", source = "nombre")
    @Mapping(target = "category", source = "categoria")
    @Mapping(target = "imageUrl", source = "imagen")
    @Mapping(target = "price", source = "precio")
    @Mapping(target = "provider", expression = "java(getProvider())")
    ProductDTO toProductDTO(CarrefourProductDTO carrefourProduct);

    default List<ProductDTO> toProductDTOs(List<CarrefourProductDTO> carrefourProducts) {
        return carrefourProducts.stream()
                .map(this::toProductDTO)
                .toList();
    }

    default String getProvider() {
        return SuperMarketEnum.CARREFOUR.toString();
    }
}