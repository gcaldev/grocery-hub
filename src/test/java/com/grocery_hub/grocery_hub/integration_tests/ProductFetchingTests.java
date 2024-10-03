package com.grocery_hub.grocery_hub.integration_tests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import com.grocery_hub.grocery_hub.TestingUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductFetchingTests {

    @Autowired private WebTestClient webTestClient;

    private static WireMockServer wireMockServer;

    private static final String ENDPOINT = "/api/products";

    @Value("${carrefour.endpoint.search}")
    private String carrefourSearchEndpoint;

    @Value("${laanonima.endpoint.search}")
    private String laAnonimaSearchEndpoint;

    @DynamicPropertySource
    static void overrideWebClientBaseUrl(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("carrefour.url", wireMockServer::baseUrl);
        dynamicPropertyRegistry.add("laanonima.url", wireMockServer::baseUrl);
        dynamicPropertyRegistry.add("dia.ftp-url", () -> "src/test/resources/mocks/DiaProductsIntegration.csv");
    }

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void clearWireMock() {
        wireMockServer.resetAll();
    }

    @Test
    @SneakyThrows
    @Order(1)
    void givenAQueryThatFiltersOnlyCarrefourProducts_whenGettingProducts_thenListOfProducts() {
        var carrefourApiResponse = TestingUtils.getFileContent("src/test/resources/mocks/CarrefourResponseIntegration.json");
        var expectedResponse = TestingUtils.getFileContent("src/test/resources/mocks/CarrefourFilterIntegrationResult.json");

        wireMockServer.stubFor(
                WireMock.get(carrefourSearchEndpoint + "?nombre_like=frut")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(carrefourApiResponse)));

        this.webTestClient
                .get()
                .uri(ENDPOINT + "?market=CARREFOUR&searchName=frut")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedResponse);
    }

    @Test
    @Order(2)
    @SneakyThrows
    void givenAQueryThatFiltersOnlyLaAnonimaProducts_whenGettingProducts_thenListOfProducts() {
        var website = TestingUtils.getFileContent("src/test/resources/mocks/LaAnonimaWebsite.html");
        var expectedResponse = TestingUtils.getFileContent("src/test/resources/mocks/LaAnonimaFilterIntegrationResult.json");
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(laAnonimaSearchEndpoint + "?pag=1&clave=frut"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.TEXT_HTML_VALUE)
                                        .withBody(website)));

        this.webTestClient
                .get()
                .uri(ENDPOINT + "?market=LA_ANONIMA&searchName=frut")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedResponse);
    }

    @Test
    @Order(3)
    @SneakyThrows
    void givenAQueryWithoutFilters_whenGettingProducts_thenListOfProducts() {
        var carrefourApiResponse = TestingUtils.getFileContent("src/test/resources/mocks/CarrefourResponseIntegration.json");
        var website = TestingUtils.getFileContent("src/test/resources/mocks/LaAnonimaWebsite.html");
        var expectedResponse = TestingUtils.getFileContent("src/test/resources/mocks/AllProductsIntegrationResult.json");
        wireMockServer.stubFor(
                WireMock.get(carrefourSearchEndpoint + "?nombre_like=frut")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(carrefourApiResponse)));
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(laAnonimaSearchEndpoint + "?pag=1&clave=frut"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.TEXT_HTML_VALUE)
                                        .withBody(website)));


        this.webTestClient
                .get()
                .uri(ENDPOINT + "?searchName=frut")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedResponse);
    }
}