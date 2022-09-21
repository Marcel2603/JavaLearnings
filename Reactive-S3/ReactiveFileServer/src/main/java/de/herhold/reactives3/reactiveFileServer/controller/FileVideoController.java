package de.herhold.reactives3.reactiveFileServer.controller;

import de.herhold.reactives3.reactiveFileServer.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RestController
public class FileVideoController {

    private final S3Service s3Service;

    public FileVideoController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping(
            path = "/downloadVideo",
            produces = "video/mp4"
    )
    public Mono<ResponseEntity<Flux<byte[]>>> downloadFileGet(
            @RequestParam(value = "key", required = true) String key,
            ServerWebExchange exchange) {
        return s3Service.downloadFile(key)
                .map((response) -> ResponseEntity.ok()
                        .body(response
                                .getFlux()
                                .map(ByteBuffer::array)
                        ));
    }
}

