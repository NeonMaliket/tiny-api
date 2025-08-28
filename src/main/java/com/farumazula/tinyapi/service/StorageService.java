package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.DocumentMetadataDto;
import com.farumazula.tinyapi.entity.DocumentMetadata;
import com.farumazula.tinyapi.repository.DocumentMetadataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.farumazula.tinyapi.utils.ApplicationConstants.MAIN_STORAGE_BUCKET;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class StorageService {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;


    public Mono<DocumentMetadataDto> upload(
            final String filename,
            final Flux<FilePart> fileParts
    ) {
        log.info("Uploading file to storage: {}", filename);
        var documentType = filename.contains(".") ? filename.split("\\.")[1] : "";
        return documentMetadataRepository.save(
                        DocumentMetadata.builder()
                                .bucket(MAIN_STORAGE_BUCKET)
                                .type(documentType)
                                .fileName(filename)
                                .createdAt(LocalDateTime.now())
                                .build()
                )
                .flatMap(metadata -> documentService.saveToBucket(
                                        metadata.getId(),
                                        metadata.getBucket(),
                                        fileParts
                                )
                                .then(Mono.just(DocumentMetadataDto.fromDocumentMetadata(metadata)))
                );
    }

    public Flux<DocumentMetadataDto> storageList() {
        return documentMetadataRepository
                .findAll()
                .map(DocumentMetadataDto::fromDocumentMetadata);
    }

    public Flux<DataBuffer> download(final String docId) {
        return documentService.download(MAIN_STORAGE_BUCKET, docId);
    }

    public Mono<Void> delete(final String docId) {
        return documentMetadataRepository
                .deleteById(docId)
                .then(documentService.delete(MAIN_STORAGE_BUCKET, docId));
    }
}
