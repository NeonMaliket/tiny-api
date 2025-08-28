package com.farumazula.tinyapi.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.farumazula.tinyapi.utils.ApplicationConstants.MAIN_STORAGE_BUCKET;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class DocumentService {

    @Autowired
    private Scheduler boundedElastic;
    @Autowired
    private MinioClient minioClient;


    @PostConstruct
    @SneakyThrows
    public void init() {
        var isMainBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MAIN_STORAGE_BUCKET).build());
        if (!isMainBucketExists) {
            log.info("Creating main bucket {}", MAIN_STORAGE_BUCKET);
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(MAIN_STORAGE_BUCKET)
                            .build()
            );
        }
    }

    @SneakyThrows
    public Mono<Void> createBucket(String name) {
        return Mono.<Void>fromCallable(() -> {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build()
            );
            return null;
        }).subscribeOn(boundedElastic);
    }


    public Mono<Void> saveToBucket(String bucket, Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(filePart -> DataBufferUtils.join(filePart.content())
                        .flatMap(dataBuffer -> Mono.<Void>fromCallable(() -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);

                            try (var in = new ByteArrayInputStream(bytes)) {
                                minioClient.putObject(
                                        PutObjectArgs.builder()
                                                .bucket(bucket)
                                                .object(filePart.filename())
                                                .stream(in, bytes.length, -1)
                                                .build()
                                );
                            }
                            return null;
                        }).subscribeOn(boundedElastic)))
                .then();
    }

    public Flux<DataBuffer> download(String bucket, String object) {
        return Mono.fromCallable(() -> minioClient.getObject(
                        GetObjectArgs.builder().bucket(bucket).object(object).build()))
                .subscribeOn(boundedElastic)
                .flatMapMany(is ->
                        DataBufferUtils.readInputStream(() -> is,
                                        new DefaultDataBufferFactory(),
                                        64 * 1024)
                                .doFinally(sig -> Mono.fromRunnable(() -> {
                                    try { is.close(); } catch (IOException ignored) {}
                                }).subscribeOn(boundedElastic).subscribe())
                );
    }

}
