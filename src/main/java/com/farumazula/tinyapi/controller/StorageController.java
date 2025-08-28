package com.farumazula.tinyapi.controller;

import com.farumazula.tinyapi.dto.DocumentMetadataDto;
import com.farumazula.tinyapi.service.StorageService;
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
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload/{filename}")
    public Mono<DocumentMetadataDto> upload(
            @PathVariable String filename,
            @RequestPart("file") Flux<FilePart> filePartFlux
    ) {
        return storageService.upload(filename, filePartFlux);
    }

    @GetMapping
    public Flux<DocumentMetadataDto> storageList() {
        log.info("'Controller' Storage List");
        return storageService.storageList();
    }

    @GetMapping("/download/{docId}/{filename}")
    public ResponseEntity<Flux<DataBuffer>> download(
            @PathVariable String docId,
            @PathVariable String filename
    ) {
        var body = storageService.download(docId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }

    @DeleteMapping("/{docId}")
    public Mono<Void> delete(@PathVariable String docId) {
        return storageService.delete(docId);
    }

}
