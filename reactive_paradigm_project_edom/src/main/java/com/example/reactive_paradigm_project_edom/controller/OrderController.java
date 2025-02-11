package com.example.reactive_paradigm_project_edom.controller;

import com.example.reactive_paradigm_project_edom.domain.OrderInfo;
import com.example.reactive_paradigm_project_edom.service.AggregationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderController {

    private final AggregationService aggregationService;

    public OrderController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping(value = "/orders/{userId}", produces = "application/stream+json")
    public Flux<OrderInfo> getOrdersByUserId(@PathVariable String userId) {
        log.info("Recibida petición de órdenes para userId={}", userId);
        return aggregationService.getOrdersByUserId(userId);
    }
}
