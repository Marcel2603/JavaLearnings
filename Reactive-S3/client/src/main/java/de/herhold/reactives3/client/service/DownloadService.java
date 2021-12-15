package de.herhold.reactives3.client.service;

import de.herhold.reactives3.api.client.handler.DefaultApi;
import de.herhold.reactives3.api.client.model.FileInformation;
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
    private final DefaultApi defaultApi;

    public DownloadService(DefaultApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    public Flux<FileInformation> getFileInformationsForFolder(String folder) {
        return defaultApi
                .informationGet(folder);
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
        if (! directory.isDirectory()){
            directory.mkdirs();
        }
        File file = File.createTempFile(name + "_", null, directory);
        log.info("Create File {}", file.getAbsolutePath());
        defaultApi.downloadGet(path.toString()) //get Chunks of Bytes
                .doOnEach(signal -> this.storeBytes(signal, file)) // add Chunks to File
                .doOnComplete(() -> Runtime.getRuntime().gc())
                .subscribe(); // Profit
        fileInformation.setPath(file.toURI());
        return fileInformation;
    }

    @SneakyThrows(IOException.class)
    private void storeBytes(Signal<byte[]> signal, File file) {
        try (
                FileOutputStream fos = new FileOutputStream(file, true);
                FileChannel fileChannel = fos.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.wrap(Objects.requireNonNullElse(signal.get(), new byte[0]));
            fileChannel.write(buffer);
        }
    }
}
