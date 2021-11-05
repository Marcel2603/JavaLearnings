package com.example.reactives3.helper;

import com.example.reactives3.model.FluxResponse;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public class FluxResponseProvider implements AsyncResponseTransformer<GetObjectResponse, FluxResponse> {

    private FluxResponse response;

    @Override
    public CompletableFuture<FluxResponse> prepare() {
        response = new FluxResponse();
        return response.getCf();
    }

    @Override
    public void onResponse(GetObjectResponse sdkResponse) {
        this.response.setSdkResponse(sdkResponse);
    }

    @Override
    public void onStream(SdkPublisher<ByteBuffer> publisher) {
        response.setFlux(Flux.from(publisher));
        response.getCf().complete(response);
    }

    @Override
    public void exceptionOccurred(Throwable error) {
        response.getCf().completeExceptionally(error);
    }

}
