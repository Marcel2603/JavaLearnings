package de.herhold.reactives3.client.service;

import de.herhold.reactives3.api.client.handler.DefaultApi;
import de.herhold.reactives3.api.client.model.FileInformation;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.stream.Collectors;

@Service
public class DownloadService {
    private final DefaultApi defaultApi;

    public DownloadService(DefaultApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    public Flux<FileInformation> getFileInformationsForFolder(String folder) {
        return defaultApi
                .informationGet(folder, null);
    }

    @SneakyThrows(IOException.class)
    public FileInformation storeFileFromFileInformation(FileInformation fileInformation) {
        if (null == fileInformation) {
            return null;
        }

        String name = fileInformation.getName();
        URI path = fileInformation.getPath();
        if (null == name || null == path) {
            fileInformation.setName(name + "Error");
            return fileInformation;
        }
        File file = File.createTempFile(name, null, new File("/tmp/reactiveS3"));
        defaultApi.downloadGet(path.toString(), null) //get Chunks of Bytes
                .doOnEach(signal -> this.storeBytes(signal, file)) // add Chunks to File
                .subscribe(); // Profit
        fileInformation.setPath(file.toURI());
        return fileInformation;
    }

    @SneakyThrows(IOException.class)
    private void storeBytes(Signal<byte[]> signal, File file) {
        byte[] bytes = signal.get();
        if (null == bytes) {
            return;
        }
        try (
                FileOutputStream fos = new FileOutputStream(file, true);
                FileChannel fileChannel = fos.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            fileChannel.write(buffer);
        }
    }
}
