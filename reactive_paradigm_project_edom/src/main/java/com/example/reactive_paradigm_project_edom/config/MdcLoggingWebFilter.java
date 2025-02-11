package com.example.reactive_paradigm_project_edom.config;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class MdcLoggingWebFilter implements WebFilter {

    private static final String REQUEST_ID_HEADER = "requestId";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestId = request.getHeaders().getFirst(REQUEST_ID_HEADER);

        if (requestId == null) {
            requestId = "NO_REQUEST_ID";
        }

        MDC.put(REQUEST_ID_MDC_KEY, requestId);

        log.info("RequestId attaching it={}", requestId);

        return chain.filter(exchange)
                .doOnTerminate(() -> MDC.remove(REQUEST_ID_MDC_KEY));
    }
}
