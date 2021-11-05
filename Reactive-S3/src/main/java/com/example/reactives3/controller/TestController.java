package com.example.reactives3.controller;

import com.example.reactives3.model.FileInformation;
import com.example.reactives3.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.ByteBuffer;

@RestController
public class TestController {

    private final S3Service s3Service;

    public TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @RequestMapping(value = "/download",
            produces = {"text/event-stream"},
            method = RequestMethod.GET)
    public Mono<ResponseEntity<Flux<byte[]>>> testEndpoint(@RequestParam(name = "key") String key) {
        return s3Service.downloadFileTest(key)
                .map((response) -> ResponseEntity.ok()
                        .body(response
                                .getFlux()
                                .map(ByteBuffer::array)
                                .doOnComplete(() -> Runtime.getRuntime().gc())
                        ));
    }

    @GetMapping(value = "/downloadFolder/{key}")
    public Mono<ResponseEntity<Flux<FileInformation>>> testFolderEndpoint(@PathVariable(name = "key") String key) {
        return Mono.just(ResponseEntity.ok(s3Service.downloadFilesFromFolder(key)
                        .flatMap(
                                bufferFlux -> bufferFlux)
                        .flatMap(bufferFlux -> bufferFlux)
                        .map(byteBuffer -> new FileInformation("file", byteBuffer.array().length, URI.create("/empty")))
                )
        ).doOnSuccess(response -> Runtime.getRuntime().gc());
    }

    @RequestMapping(value = "/information",
            produces = {"text/event-stream"},
            method = RequestMethod.GET)
    public Mono<ResponseEntity<Flux<FileInformation>>> informationGet(
            @RequestParam(value = "folder", required = true) String folder,
            @RequestParam(value = "id", required = false) String id) {
        return Mono.just(
                ResponseEntity.ok(
                        s3Service.getFileInformationForFolder(folder)
                ));
    }
}
