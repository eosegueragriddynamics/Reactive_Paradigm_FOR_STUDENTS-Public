package com.example.reactive_paradigm_project_edom;

import com.example.reactive_paradigm_project_edom.client.OrderSearchClient;
import com.example.reactive_paradigm_project_edom.client.ProductInfoClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ReactiveParadigmProjectEdomApplicationTests {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testOrderSearchClient() {
        stubFor(get(urlPathEqualTo("/orderSearchService/order/phone"))
                .withQueryParam("phoneNumber", equalTo("111-1111"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("["
                                + "{\"orderNumber\":\"123\",\"phoneNumber\":\"111-1111\",\"productCode\":\"P001\"},"
                                + "{\"orderNumber\":\"124\",\"phoneNumber\":\"111-1111\",\"productCode\":\"P002\"}"
                                + "]")));

        OrderSearchClient client = new OrderSearchClient("http://localhost:8089");
        StepVerifier.create(client.getOrdersByPhone("111-1111"))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testProductInfoClient() {
        stubFor(get(urlPathEqualTo("/productInfoService/product/names"))
                .withQueryParam("productCode", equalTo("P001"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("["
                                + "{\"productId\":\"X1\",\"productName\":\"Product X1\",\"score\":4.5},"
                                + "{\"productId\":\"X2\",\"productName\":\"Product X2\",\"score\":5.0}"
                                + "]")));

        ProductInfoClient client = new ProductInfoClient("http://localhost:8089", "5s");
        StepVerifier.create(client.getProductInfoList("P001"))
                .expectNextMatches(list -> list.size() == 2)
                .verifyComplete();
    }
}
