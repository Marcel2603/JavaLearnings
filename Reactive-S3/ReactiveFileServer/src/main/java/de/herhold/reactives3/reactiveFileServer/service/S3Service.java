package de.herhold.reactives3.reactiveFileServer.service;

import de.herhold.reactives3.api.fileApi.reactiveServer.model.FileContent;
import de.herhold.reactives3.reactiveFileServer.config.S3ClientConfigurarionProperties;
import de.herhold.reactives3.reactiveFileServer.helper.FluxResponseProvider;
import de.herhold.reactives3.reactiveFileServer.model.FileInformation;
import de.herhold.reactives3.reactiveFileServer.model.FluxResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class S3Service {
    private final S3AsyncClient s3client;
    private final S3ClientConfigurarionProperties s3config;

    public S3Service(S3AsyncClient s3client, S3ClientConfigurarionProperties s3config) {
        this.s3client = s3client;
        this.s3config = s3config;
    }

    public Mono<FluxResponse> downloadFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3config.getBucket())
                .key(key)
                .build();

        return Mono.fromFuture(s3client.getObject(request, new FluxResponseProvider()));
    }

    public Flux<FileInformation> getFileInformationForFolder(String folder) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(s3config.getBucket())
                .prefix(folder + "/")
                .build();

        CompletableFuture<ListObjectsV2Response> listObjectsV2ResponseCompletableFuture = s3client.listObjectsV2(listObjectsV2Request);

        return Mono.fromFuture(listObjectsV2ResponseCompletableFuture)
                .flux()
                .flatMap(listObjectsV2Response -> Flux.fromIterable(listObjectsV2Response.contents()))
                .map(this::getFileInformation);
    }

    public Flux<FileContent> getFiles(String folder) {
        return this.getFileInformationForFolder(folder).map(this::mapFileInformationToContent);
    }

    private FileContent mapFileInformationToContent(FileInformation fileInformation) {
        FileContent fileContent = new FileContent().name(fileInformation.getName()).path(fileInformation.getPath());
        long contentLength = fileInformation.getSize();
        ByteBuffer byteBuffer = ByteBuffer.allocate(Math.toIntExact(contentLength));
        this.downloadFile(fileContent.getPath().toString()).doOnNext(fluxResponse -> {
            fluxResponse.getFlux().doOnEach(byteBufferSignal -> byteBuffer.put(byteBufferSignal.get()));
        }).subscribe();
        return fileContent.content(byteBuffer.array());
    }

    private FileInformation getFileInformation(S3Object s3Object) {
        return new FileInformation(Path.of(s3Object.key()).getFileName().toString(), s3Object.size(), URI.create(s3Object.key()));
    }

}
