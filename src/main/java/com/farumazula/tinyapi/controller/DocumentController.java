package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.service.DocumentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Ma1iket
 **/

@Log4j2
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @PostMapping("/bucket/{name}")
    public Mono<Void> createBucket(@PathVariable String name) {
        log.info("Creating bucket {}", name);
        return documentService.createBucket(name);
    }

    @PostMapping("/bucket/{bucket}/save")
    public Mono<Void> saveToBucket(@PathVariable String bucket,
                                   @RequestPart("file") Flux<FilePart> filePartFlux) {
        log.info("Saving file to bucket {}", bucket);
        return documentService.saveToBucket(bucket, filePartFlux);
    }

    @GetMapping("/bucket/{bucket}/{fileName}")
    public ResponseEntity<Flux<DataBuffer>> download(@PathVariable String bucket,
                                                     @PathVariable String fileName) {
        Flux<DataBuffer> body = documentService.download(bucket, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }

}
