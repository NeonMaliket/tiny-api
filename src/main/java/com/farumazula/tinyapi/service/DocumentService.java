package com.farumazula.tinyapi.service;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.ByteArrayInputStream;

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


}
