package com.example.reactive_paradigm_project_edom.service;

import com.example.reactive_paradigm_project_edom.client.OrderSearchClient;
import com.example.reactive_paradigm_project_edom.client.ProductInfoClient;
import com.example.reactive_paradigm_project_edom.domain.OrderInfo;
import com.example.reactive_paradigm_project_edom.domain.ProductInfo;
import com.example.reactive_paradigm_project_edom.domain.UserInfo;
import com.example.reactive_paradigm_project_edom.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class AggregationService {

    private final UserInfoRepository userInfoRepository;
    private final OrderSearchClient orderSearchClient;
    private final ProductInfoClient productInfoClient;

    public AggregationService(UserInfoRepository userInfoRepository,
                              OrderSearchClient orderSearchClient,
                              ProductInfoClient productInfoClient) {
        this.userInfoRepository = userInfoRepository;
        this.orderSearchClient = orderSearchClient;
        this.productInfoClient = productInfoClient;
    }

    public Flux<OrderInfo> getOrdersByUserId(String userId) {
        return userInfoRepository.findById(userId)
                .flatMapMany(userInfo -> orderSearchClient.getOrdersByPhone(userInfo.getPhoneNumber())
                        .flatMap(order -> enrichWithProductInfo(order, userInfo)))
                .doOnSubscribe(sub -> log.info("Buscando órdenes para userId={}", userId))
                .doOnError(err -> log.error("Error al obtener órdenes para userId={}: {}", userId, err.toString()));
    }

    private Mono<OrderInfo> enrichWithProductInfo(OrderInfo order, UserInfo userInfo) {
        return productInfoClient.getProductInfoList(order.getProductCode())
                .map(this::getMaxScoreProduct)
                .map(maxProduct -> {
                    order.setPhoneNumber(userInfo.getPhoneNumber());
                    order.setUserName(userInfo.getUserName());

                    if (maxProduct != null) {
                        order.setProductId(maxProduct.getProductId());
                        order.setProductName(maxProduct.getProductName());
                    }

                    return order;
                })
                .doOnNext(o -> log.info("Orden enriquecida: {}", o))
                .onErrorReturn(order);
    }

    private ProductInfo getMaxScoreProduct(List<ProductInfo> productList) {
        if (productList == null || productList.isEmpty()) {
            return null;
        }
        return productList.stream()
                .max(Comparator.comparingDouble(ProductInfo::getScore))
                .orElse(null);
    }
}
