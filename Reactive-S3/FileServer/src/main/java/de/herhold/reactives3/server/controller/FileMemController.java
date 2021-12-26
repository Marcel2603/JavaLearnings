package de.herhold.reactives3.server.controller;

import de.herhold.reactives3.api.fileApi.server.handler.FileMemApi;
import de.herhold.reactives3.api.fileApi.server.model.FileContent;
import de.herhold.reactives3.server.mapper.FileContentMapper;
import de.herhold.reactives3.server.model.FileInformation;
import de.herhold.reactives3.server.service.S3Service;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileMemController implements FileMemApi {
    private final S3Service s3Service;

    public FileMemController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public ResponseEntity<List<FileContent>> downloadFilesGet(
            @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "folder", required = true) String folder
    ) {
        return ResponseEntity.ok(s3Service.getFileInformationForFolder(folder)
                .map(this::addContentToFileInformation)
                .collect(Collectors.toList()));
    }

    @SneakyThrows(IOException.class)
    private FileContent addContentToFileInformation(FileInformation fileInformation) {
        FileContent fileContent = FileContentMapper.INSTANCE.mapFileInformationToContent(fileInformation);
        return fileContent.content(s3Service.getBytesForKey(fileContent.getPath().toString()).readAllBytes());
    }
}
