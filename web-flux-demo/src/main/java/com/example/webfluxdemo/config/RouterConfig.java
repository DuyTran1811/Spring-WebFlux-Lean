package com.example.webfluxdemo.config;

import com.example.webfluxdemo.dto.InputFailedValidationResponse;
import com.example.webfluxdemo.exception.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    private final RequestHandler requestHandler;

    public RouterConfig(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter() {
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("/square/{input}", RequestPredicates.path("*/1?")
                        .or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("/square/{input}", eqp -> ServerResponse.badRequest().bodyValue("only 10 - 19 all"))
                .GET("/table/{input}", requestHandler::tableHandler)
                .GET("/table/{input}/stream", requestHandler::tableStreamHandler)
                .GET("/square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .POST("/multiply", requestHandler::multiplyHandler)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, rep) -> {
            InputValidationException exception = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(exception.getInput());
            response.setMessage(exception.getMessage());
            response.setErrorCode(exception.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }
}
