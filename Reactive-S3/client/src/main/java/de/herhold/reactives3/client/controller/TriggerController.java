package de.herhold.reactives3.client.controller;

import de.herhold.reactives3.api.client.model.FileInformation;
import de.herhold.reactives3.client.service.DownloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TriggerController {

    private final DownloadService downloadService;

    public TriggerController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping(value = "/trigger")
    public Mono<ResponseEntity<Flux<FileInformation>>> triggerDownload(@RequestParam(value = "folder") String folder) {
        return Mono.just(
                ResponseEntity.ok(
                        downloadService.getFileInformationsForFolder(folder)
                                .map(downloadService::storeFileFromFileInformation)
                )
        );
    }
}
