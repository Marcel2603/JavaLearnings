package de.herhold.reactives3.reactiveFileServer.exception;

import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class DownloadFailedException extends RuntimeException {
    public DownloadFailedException(GetObjectResponse response) {
        super(response.toString());
    }
}
