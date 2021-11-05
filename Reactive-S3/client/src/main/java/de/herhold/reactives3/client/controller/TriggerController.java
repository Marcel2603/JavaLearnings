package de.herhold.reactives3.client.controller;

import de.herhold.reactives3.api.client.handler.DefaultApi;
import de.herhold.reactives3.api.client.model.FileInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TriggerController {

    private final DefaultApi defaultApi;

    public TriggerController(DefaultApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    @GetMapping(value = "/trigger")
    public Mono<ResponseEntity<Flux<FileInformation>>> triggerDownload(@RequestParam(value = "folder") String folder) {
        return Mono.just(ResponseEntity.ok(defaultApi
                .informationGet("test2", null)));
    }

}
