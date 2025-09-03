package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.DocumentMetadataDto;
import com.farumazula.tinyapi.entity.DocumentMetadata;
import com.farumazula.tinyapi.events.StreamEvent;
import com.farumazula.tinyapi.repository.DocumentMetadataRepository;
import com.mongodb.client.model.changestream.OperationType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.codec.ServerSentEvent;
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
    @Autowired
    private ReactiveMongoTemplate mongo;


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
                                .then(Mono.just(DocumentMetadataDto.from(metadata)))
                );
    }

    public Flux<ServerSentEvent<DocumentMetadataDto>> storageList() {
        var historyDocuments = documentMetadataRepository
                .findAll()
                .map(DocumentMetadataDto::from)
                .map(StreamEvent.HISTORY::toSentEvent);
        var live = mongo
                .changeStream(DocumentMetadata.class)
                .watchCollection(DocumentMetadata.class)
                .listen()
                .filter(x -> x.getOperationType() == OperationType.DELETE || x.getOperationType() == OperationType.INSERT)
                .map(entry -> {
                    var body = entry.getBody();
                    if (entry.getOperationType() != OperationType.DELETE && body != null) {
                        return StreamEvent.NEW.toSentEvent(DocumentMetadataDto.from(body));
                    } else {
                        return StreamEvent.DELETE.toSentEvent(DocumentMetadataDto.builder()
                                        .id(entry.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toString())
                                .build());
                    }
                });
        return Flux.concat(historyDocuments, live);
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
