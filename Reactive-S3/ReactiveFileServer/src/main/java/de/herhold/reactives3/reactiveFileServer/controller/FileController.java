package de.herhold.reactives3.reactiveFileServer.controller;

import de.herhold.reactives3.api.fileApi.reactiveServer.handler.FileApi;
import de.herhold.reactives3.api.fileApi.reactiveServer.model.FileInformation;
import de.herhold.reactives3.reactiveFileServer.helper.mapping.FileInformationMapper;
import de.herhold.reactives3.reactiveFileServer.service.S3Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FileController implements FileApi {

    private final S3Service s3Service;
    private final MeterRegistry meterRegistry;
    private AtomicLong lastTransferedFileBytes;
    private Counter total_transfered_file_bytes;

    public FileController(S3Service s3Service, MeterRegistry meterRegistry) {
        this.s3Service = s3Service;
        this.meterRegistry = meterRegistry;
        buildGauges();
    }

    private void buildGauges() {
        this.lastTransferedFileBytes = new AtomicLong();
        this.total_transfered_file_bytes = Counter.builder("total_transfered_file_bytes").description("Display the total size of bytes tranfered").register(meterRegistry);
        Gauge.builder("last_transfered_file_bytes", this.lastTransferedFileBytes, AtomicLong::floatValue).description("Displays the size of the last returned File in bytes").register(meterRegistry);
    }


    @Override
    public Mono<ResponseEntity<Flux<byte[]>>> downloadFileGet(
            @RequestParam(value = "key", required = true) String key,
            ServerWebExchange exchange) {
        AtomicLong size = new AtomicLong();
        return s3Service.downloadFile(key)
                .map((response) -> {
                    size.set(response.getSdkResponse().contentLength());
                    return ResponseEntity.ok()
                            .body(response
                                    .getFlux()
                                    .map(ByteBuffer::array)
                                    .doOnComplete(() -> {
                                        this.lastTransferedFileBytes.set(size.get());
                                        this.total_transfered_file_bytes.increment(size.floatValue());
                                        Runtime.getRuntime().gc();
                                    })
                            );
                });
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
