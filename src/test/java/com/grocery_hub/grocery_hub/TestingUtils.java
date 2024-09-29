package com.grocery_hub.grocery_hub;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;


public class TestingUtils {
    public <T> T getConfiguracionFromFile(String fileName, TypeReference<T> clazz) throws URISyntaxException, IOException {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        URL res = getClass().getClassLoader().getResource(fileName);
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return objectMapper.readValue(file, clazz);
    }
}
