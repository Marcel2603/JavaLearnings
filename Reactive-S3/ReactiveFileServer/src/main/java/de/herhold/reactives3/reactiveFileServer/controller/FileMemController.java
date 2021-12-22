package de.herhold.reactives3.reactiveFileServer.controller;

import de.herhold.reactives3.api.fileApi.reactiveServer.handler.FileMemApi;
import de.herhold.reactives3.api.fileApi.reactiveServer.model.FileContent;
import de.herhold.reactives3.reactiveFileServer.service.S3Service;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class FileMemController implements FileMemApi {
    private final S3Service s3Service;

    public FileMemController(S3Service s3Service) {
        this.s3Service = s3Service;
    }


    public Mono<ResponseEntity<Flux<FileContent>>> filesGet(
            @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "folder", required = true) String folder,
            ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(200).body(s3Service.getFiles(folder)));

    }
}
