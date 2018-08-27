package org.superbiz.moviefun.BlobStore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {


    private AmazonS3Client s3Client;
    private String photoStorageBucket;
    private Tika tika = new Tika();

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        PutObjectRequest request = new PutObjectRequest(photoStorageBucket, blob.name, blob.inputStream,null);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(blob.contentType);
        metadata.addUserMetadata("x-amz-meta-title", "someTitle");
        request.setMetadata(metadata);
        s3Client.putObject(request);

    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        S3Object s3Object = s3Client.getObject(photoStorageBucket, name);
        S3ObjectInputStream content = s3Object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(content);

        return Optional.of(new Blob(
                name,
                new ByteArrayInputStream(bytes),
                tika.detect(bytes)
        ));
    }

    @Override
    public void deleteAll() {
        s3Client.deleteBucket(photoStorageBucket);

    }
}
