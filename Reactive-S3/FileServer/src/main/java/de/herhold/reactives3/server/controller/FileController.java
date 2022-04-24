package de.herhold.reactives3.server.controller;

import de.herhold.reactives3.api.fileApi.server.handler.FileApi;
import de.herhold.reactives3.api.fileApi.server.model.FileInformation;
import de.herhold.reactives3.server.mapper.FileInformationMapper;
import de.herhold.reactives3.server.service.S3Service;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController implements FileApi {
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public ResponseEntity<List<FileInformation>> informationGet(
            @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "folder", required = true) String folder
    ) {
        return ResponseEntity.ok(
                s3Service.getFileInformationForFolder(folder)
                        .map(FileInformationMapper.INSTANCE::mapFileInformationToApi)
                        .collect(Collectors.toList())
        );
    }

    @SneakyThrows(IOException.class)
    @Override
    public ResponseEntity<byte[]> downloadFileGet(
            @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "key", required = true) String key
    ) {
        InputStream bytesForKey = s3Service.getBytesForKey(key);
        byte[] content = bytesForKey.readAllBytes();
        bytesForKey.close();
        return ResponseEntity.ok(content);
    }
}