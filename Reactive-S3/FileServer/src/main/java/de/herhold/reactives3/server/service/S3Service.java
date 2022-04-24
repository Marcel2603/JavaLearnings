package de.herhold.reactives3.server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import de.herhold.reactives3.server.config.S3ClientConfigurarionProperties;
import de.herhold.reactives3.server.model.FileInformation;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    private final S3ClientConfigurarionProperties s3config;

    public S3Service(AmazonS3 amazonS3, S3ClientConfigurarionProperties s3config) {
        this.amazonS3 = amazonS3;
        this.s3config = s3config;
    }

    public Stream<FileInformation> getFileInformationForFolder(String folder) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
                .withBucketName(s3config.getBucket() + "/")
                .withPrefix(folder + "/");

        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(listObjectsV2Request);
        return this.transformToStream(listObjectsV2Result.getObjectSummaries());
    }

    public InputStream getBytesForKey(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(s3config.getBucket(), s3config.getBucket() + "/" + key);
        return amazonS3.getObject(getObjectRequest).getObjectContent();
    }

    private Stream<FileInformation> transformToStream(List<S3ObjectSummary> objectSummaries) {
        return objectSummaries.stream().map(
                s3ObjectSummary -> new FileInformation(
                        Path.of(s3ObjectSummary.getKey()).getFileName().toString(),
                        s3ObjectSummary.getSize(),
                        URI.create(s3ObjectSummary.getKey())
                ));
    }
}
