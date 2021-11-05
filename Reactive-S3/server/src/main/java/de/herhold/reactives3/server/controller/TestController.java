package de.herhold.reactives3.server.controller;

import de.herhold.reactives3.api.server.handler.DefaultApi;
import de.herhold.reactives3.api.server.model.FileInformation;
import de.herhold.reactives3.server.helper.mapping.FileInformationMapper;
import de.herhold.reactives3.server.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.ByteBuffer;

@RestController
public class TestController implements DefaultApi {

    private final S3Service s3Service;

    public TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public Mono<ResponseEntity<Flux<byte[]>>> downloadGet(
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "id", required = false) String id,
            ServerWebExchange exchange) {
        return s3Service.downloadFileTest(key)
                .map((response) -> ResponseEntity.ok()
                        .body(response
                                .getFlux()
                                .map(ByteBuffer::array)
                                .doOnComplete(() -> Runtime.getRuntime().gc())
                        ));
    }

    @Override
    public Mono<ResponseEntity<Flux<FileInformation>>> informationGet(
            @RequestParam(value = "folder", required = true) String folder,
            @RequestParam(value = "id", required = false) String id,
            ServerWebExchange serverWebExchange) {
        return Mono.just(
                ResponseEntity.ok(
                        s3Service.getFileInformationForFolder(folder)
                                .map(FileInformationMapper.INSTANCE::mapModelToApi)
                ));
    }

    @Deprecated
    @GetMapping(value = "/downloadFolder/{key}")
    public Mono<ResponseEntity<Flux<FileInformation>>> testFolderEndpoint(@PathVariable(name = "key") String key) {
        return Mono.just(ResponseEntity.ok(s3Service.downloadFilesFromFolder(key)
                        .flatMap(
                                bufferFlux -> bufferFlux)
                        .flatMap(bufferFlux -> bufferFlux)
                        .map(byteBuffer -> new de.herhold.reactives3.server.model.FileInformation("file", byteBuffer.array().length, URI.create("/empty")))
                        .map(FileInformationMapper.INSTANCE::mapModelToApi)
                )
        ).doOnSuccess(response -> Runtime.getRuntime().gc());
    }
}
