package com.example.webfluxdemo.controller;

import com.example.webfluxdemo.dto.MultiplyRequestDto;
import com.example.webfluxdemo.dto.Response;
import com.example.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/reactive-math")
public class ReactiveController {

    private final ReactiveMathService reactiveMathService;

    public ReactiveController(ReactiveMathService reactiveMathService) {
        this.reactiveMathService = reactiveMathService;
    }

    @GetMapping("/square/{input}")
    public Mono<Response> findSquare(@PathVariable int input) {
        return this.reactiveMathService.findSquare(input);
    }

    @GetMapping("/table/{input}")
    public Flux<Response> multiplicationTable(@PathVariable int input) {
        return this.reactiveMathService.multiplicationTable(input);
    }

    @GetMapping(value = "/table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> multiplicationTableStream(@PathVariable int input) {
        return this.reactiveMathService.multiplicationTable(input);
    }

    @PostMapping("/multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> requestDtoMono,
                                   @RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        return this.reactiveMathService.multiply(requestDtoMono);
    }
}
