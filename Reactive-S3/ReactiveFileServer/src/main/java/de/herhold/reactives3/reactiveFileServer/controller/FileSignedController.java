package de.herhold.reactives3.reactiveFileServer.controller;

import de.herhold.reactives3.reactiveFileServer.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URL;

@RestController
public class FileSignedController {
    private final S3Service s3Service;

    public FileSignedController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping(path = "/link/file")
    public Mono<ResponseEntity<URL>> retrievedSignedUrl(
            @RequestParam(value = "key", required = true) String key,
            ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().headers(
                httpHeaders -> httpHeaders.add("Access-Control-Allow-Origin","*")
        ).body(s3Service.getLink(key)));
    }
}
