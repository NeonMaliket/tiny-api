package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.service.DocumentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
        return documentService.saveToBucket(bucket, filePartFlux);
    }
}
