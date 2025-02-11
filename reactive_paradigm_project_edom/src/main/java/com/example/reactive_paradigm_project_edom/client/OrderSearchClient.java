package com.example.reactive_paradigm_project_edom.client;

import com.example.reactive_paradigm_project_edom.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class OrderSearchClient {

    private final WebClient webClient;

    public OrderSearchClient(@Value("${external.order-search-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<OrderInfo> getOrdersByPhone(String phoneNumber) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/orderSearchService/order/phone")
                                .queryParam("phoneNumber", phoneNumber)
                                .build())
                .retrieve()
                .bodyToFlux(OrderInfo.class);
    }
}
