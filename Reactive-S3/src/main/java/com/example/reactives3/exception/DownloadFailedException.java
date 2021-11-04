package com.example.reactives3.exception;

import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class DownloadFailedException extends RuntimeException {
    public DownloadFailedException(GetObjectResponse response) {
        super(response.toString());
    }
}
