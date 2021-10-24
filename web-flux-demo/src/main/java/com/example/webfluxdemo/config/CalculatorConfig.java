package com.example.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class CalculatorConfig {
    private final CalculatorHandler handler;

    public CalculatorConfig(CalculatorHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> highLevelCalculatorRouter() {
        return RouterFunctions.route()
                .path("calculator", this::serverResponseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("/{a}/{b}", isOperation("+"), handler::additionHandler)
                .GET("/{a}/{b}", isOperation("-"), handler::subtractionHandler)
                .GET("/{a}/{b}", isOperation("*"), handler::multiplicationHandler)
                .GET("/{a}/{b}", isOperation("/"), handler::divisionHandler)
                .GET("/{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be + - * /"))

                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders()
                .toSingleValueMap()
                .get("OP")));
    }
}
