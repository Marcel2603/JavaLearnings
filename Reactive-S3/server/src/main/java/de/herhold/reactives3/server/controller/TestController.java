package de.herhold.reactives3.server.controller;

import de.herhold.reactives3.api.server.handler.DefaultApi;
import de.herhold.reactives3.api.server.model.FileInformation;
import de.herhold.reactives3.server.helper.mapping.FileInformationMapper;
import de.herhold.reactives3.server.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
            ServerWebExchange exchange) {
        return s3Service.downloadFile(key)
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
            ServerWebExchange serverWebExchange) {
        return Mono.just(
                ResponseEntity.ok(
                        s3Service.getFileInformationForFolder(folder)
                                .map(FileInformationMapper.INSTANCE::mapModelToApi)
                ));
    }
}
