package com.grocery_hub.grocery_hub.mapper;
import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.model.CarrefourProductDTO;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaProductMapper {

    public default ProductDTO toProductDTO(String line) throws NumberFormatException{
        String[] values = line.split(",");
        if (values.length < 13) {
            return null;
        }

        return ProductDTO.builder()
                .id(values[0])
                .name(values[1])
                .category(values[3])
                .price(Double.parseDouble(values[4]))
                .stock(Integer.parseInt(values[12]))
                .imageUrl(values[7])
                .provider(getProvider())
                .build();
    }

    default String getProvider() {
        return SuperMarketEnum.DIA.toString();
    }
}
