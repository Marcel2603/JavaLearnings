package de.herhold.reactives3.reactiveClient.service;

import de.herhold.reactives3.api.fileApi.reactiveClient.handler.FileApi;
import de.herhold.reactives3.api.fileApi.reactiveClient.handler.FileMemApi;
import de.herhold.reactives3.api.fileApi.reactiveClient.model.FileContent;
import de.herhold.reactives3.api.fileApi.reactiveClient.model.FileInformation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

@Service
@Log4j2
public class DownloadService {
    private final FileApi fileApi;
    private final FileMemApi fileMemApi;

    public DownloadService(FileApi fileApi, FileMemApi fileMemApi) {
        this.fileApi = fileApi;
        this.fileMemApi = fileMemApi;
    }

    public Flux<FileInformation> getFileInformationsForFolder(String folder) {
        return fileApi
                .informationGet(folder);
    }

    public Flux<FileContent> getFileContentsForFolder(String folder) {
        return fileMemApi.filesGet(folder).doOnEach(this::storeFileFromFileContent);
    }

    @SneakyThrows(IOException.class)
    private void storeFileFromFileContent(Signal<FileContent> fileContentSignal) {
        FileContent fileContent = fileContentSignal.get();
        if (null == fileContent) {
            return;
        }
        String name = fileContent.getName();
        File directory = new File("/tmp/reactiveS3");
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = File.createTempFile(name + "_", null, directory);
        storeBytes(fileContent.getContent(), file);
    }

    @SneakyThrows(IOException.class)
    public FileInformation storeFileFromFileInformation(Signal<FileInformation> fileInformationSignal) {
        FileInformation fileInformation = fileInformationSignal.get();
        if (null == fileInformation) {
            return null;
        }

        String name = fileInformation.getName();
        URI path = fileInformation.getPath();
        if (null == name || null == path) {
            fileInformation.setName(name + "Error");
            return fileInformation;
        }
        File directory = new File("/tmp/reactiveS3");
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = File.createTempFile(name + "_", null, directory);
        log.info("Create File {}", file.getAbsolutePath());
        fileApi.downloadGet(path.toString()) //get Chunks of Bytes
                .doOnEach(signal -> this.storeBytes(signal, file)) // add Chunks to File
                .doOnComplete(() -> Runtime.getRuntime().gc())
                .subscribe(); // Profit
        fileInformation.setPath(file.toURI());
        return fileInformation;
    }

    private void storeBytes(Signal<byte[]> signal, File file) {
        storeBytes(Objects.requireNonNullElse(signal.get(), new byte[0]), file);
    }

    @SneakyThrows(IOException.class)
    private void storeBytes(byte[] bytes, File file) {
        try (
                FileOutputStream fos = new FileOutputStream(file, true);
                FileChannel fileChannel = fos.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            fileChannel.write(buffer);
        }
    }
}
