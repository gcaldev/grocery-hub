package com.grocery_hub.grocery_hub.service.impl;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.mapper.DiaProductMapper;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class DiaServiceImpl  implements ProductService {
    @Value("${dia.ftp-url}")
    private String FTP_URL;

    private final DiaProductMapper diaProductMapper;

    @Autowired
    public DiaServiceImpl(DiaProductMapper diaProductMapper) {
        this.diaProductMapper = diaProductMapper;
    }

    @Override
    public boolean isGivenMarket(SuperMarketEnum marketType) {
        return SuperMarketEnum.DIA.equals(marketType);
    }

    @Override
    public List<ProductDTO> getProducts(String searchName) {
            try (Stream<String> stream = Files.lines(Paths.get(FTP_URL))) {
                return stream
                        .skip(1)
                        .parallel()
                        .map(this::parseLine)
                        .filter(Objects::nonNull)
                        .filter(x -> x.getName().toLowerCase().contains(searchName.toLowerCase()))
                        .toList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    private ProductDTO parseLine(String line) {
        try {
            return diaProductMapper.toProductDTO(line);
        } catch (RuntimeException e) {
            System.out.println("Error parsing line: " + line);
            return null;
        }
    }
}
