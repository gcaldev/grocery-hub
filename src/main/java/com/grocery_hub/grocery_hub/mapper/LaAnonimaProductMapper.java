package com.grocery_hub.grocery_hub.mapper;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.LaAnonimaProductDTO;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LaAnonimaProductMapper {
    @Mapping(target = "provider", expression = "java(getProvider())")
    @Mapping(target = "category", expression = "java(emptyStringToNull(laAnonimaProduct.getCategory()))")
    ProductDTO toProductDTO(LaAnonimaProductDTO laAnonimaProduct);

    default Double mapStringToDouble(String value) {
        return Double.parseDouble(value);
    }

    default String emptyStringToNull(String value) {
        return value != null && value.trim().isEmpty() ? null : value;
    }

    default String getProvider() {
        return SuperMarketEnum.LA_ANONIMA.toString();
    }

}
