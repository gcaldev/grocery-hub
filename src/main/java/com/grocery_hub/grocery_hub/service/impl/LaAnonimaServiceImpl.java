package com.grocery_hub.grocery_hub.service.impl;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.mapper.LaAnonimaProductMapper;
import com.grocery_hub.grocery_hub.model.DataLayerDTO;
import com.grocery_hub.grocery_hub.model.LaAnonimaProductDTO;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class LaAnonimaServiceImpl implements ProductService {
    private LaAnonimaProductMapper laAnonimaProductMapper;

    @Value("${laanonima.url}")
    private String URL;

    @Value("${laanonima.endpoint.search}")
    private String ENDPOINT;

    @Autowired
    public LaAnonimaServiceImpl(LaAnonimaProductMapper laAnonimaProductMapper) {
        this.laAnonimaProductMapper = laAnonimaProductMapper;
    }

    @Override
    public boolean isGivenMarket(SuperMarketEnum marketType) {
        return SuperMarketEnum.LA_ANONIMA.equals(marketType);
    }


    private String getProductImage(String id, Document doc) {
        Element productDiv = doc.getElementById("prod_" + id);
        if (productDiv == null) {
            return null;
        }
        Element img = productDiv.getElementsByTag("img").first();
        return img != null ? img.attr("data-src") : null;
    }

    private ProductDTO parseProduct(LaAnonimaProductDTO product,Document doc) {
        try {
            ProductDTO productDTO = laAnonimaProductMapper.toProductDTO(product);
            if(productDTO.getPrice() <= 0) {
                return null;
            }
            productDTO.setImageUrl(getProductImage(product.getId(), doc));

            return productDTO;
        } catch (Exception e) {
            System.out.println("Error parsing product from " + SuperMarketEnum.LA_ANONIMA.toString());
            return null;
        }
    }

    private String getDataLayerScriptText(Document doc) {
        Elements scriptTags = doc.getElementsByTag("script");

        return scriptTags
                .stream()
                .map(Element::data)
                .filter(x -> x.contains("dataLayer.push"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No dataLayer found."));
        /*Element dataLayerScript = null;

        for (Element scriptTag : scriptTags) {
            if (scriptTag.data().contains("dataLayer.push")) {
                dataLayerScript = scriptTag;
                break;
            }
        }
        return dataLayerScript;*/
    }

    private DataLayerDTO parseDataLayer(String dataLayerScript) {
        Pattern pattern = Pattern.compile("dataLayer.push\\((.*?)\\);", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dataLayerScript);

        if(!matcher.find()) {
            throw new RuntimeException("Not found");
        }

        String jsonData = matcher.group(1); // Extracted content
        jsonData = jsonData.replace("'", "\"").replaceAll(",\\s*}", "}").replaceAll(",\\s*]", "]");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonData, DataLayerDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing dataLayer JSON.");
        }
    }

    private List<ProductDTO> processDataLayer(DataLayerDTO dataLayer, Document doc) {
        return dataLayer
                .getEcommerce()
                .getImpressions()
                .stream()
                .map(x -> parseProduct(x,doc))
                .filter(Objects::nonNull)
                .toList();
    }
    private String getUri(String productName) {
        return UriComponentsBuilder.fromHttpUrl(URL).path(ENDPOINT)
                .queryParam("pag", 1)
                .queryParam("clave", productName)
                .build().toUriString();
    }
    private Document connectToLaAnonima(String searchName) {
        try{
            Thread.sleep(5000);

            String url = getUri(searchName);
            return Jsoup.connect(url)
                    .followRedirects(true)
                    .get();
        } catch(IOException e) {
            throw new RuntimeException("Error connecting to La Anonima.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductDTO> getProducts(String searchName) {
        Document doc = connectToLaAnonima(searchName);
        String scriptContent = getDataLayerScriptText(doc);
        DataLayerDTO dataLayer = parseDataLayer(scriptContent);
        return processDataLayer(dataLayer, doc);
    }
}
/*
if (dataLayerScript != null) {
                // Extract the dataLayer content using regex
                Pattern pattern = Pattern.compile("dataLayer.push\\((.*?)\\);", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(dataLayerScript);
                if (matcher.find()) {
                    String jsonData = matcher.group(1); // Extracted content
                    jsonData = jsonData.replace("'", "\"").replaceAll(",\\s*}", "}").replaceAll(",\\s*]", "]");

                    // Parse the JSON data
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        //JsonNode dataLayer = objectMapper.readTree(jsonData);
                        DataLayerDTO abastecedorProductDTO = objectMapper.readValue(jsonData, DataLayerDTO.class);
                        return abastecedorProductDTO
                                .getEcommerce()
                                .getImpressions()
                                .stream()
                                .map(this::parseProduct)
                                .toList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No dataLayer found.");
            }

 */