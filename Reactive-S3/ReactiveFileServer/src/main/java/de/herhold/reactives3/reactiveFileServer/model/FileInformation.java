package de.herhold.reactives3.reactiveFileServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;

@Data
@AllArgsConstructor
public class FileInformation {
    private String name;
    private long size;
    private URI path;
}
