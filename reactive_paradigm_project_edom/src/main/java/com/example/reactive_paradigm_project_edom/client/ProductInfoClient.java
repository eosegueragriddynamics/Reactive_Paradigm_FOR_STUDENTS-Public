package com.example.reactive_paradigm_project_edom.client;

import com.example.reactive_paradigm_project_edom.domain.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class ProductInfoClient {

    private final WebClient webClient;
    private final Duration timeoutDuration;

    public ProductInfoClient(@Value("${external.product-info-service.url}") String baseUrl,
                             @Value("${external.product-info-service.timeout:5s}") String timeout) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.timeoutDuration = Duration.parse("PT" + timeout.replace("s","S"));
    }

    public Mono<List<ProductInfo>> getProductInfoList(String productCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/productInfoService/product/names")
                        .queryParam("productCode", productCode)
                        .build())
                .retrieve()
                .bodyToFlux(ProductInfo.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                .collectList()
                .timeout(timeoutDuration)
                .onErrorResume(ex -> {
                    log.error("Error al obtener product info para code {}: {}", productCode, ex.toString());
                    return Mono.just(Collections.emptyList());
                });
    }
}
