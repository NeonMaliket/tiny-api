package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.entity.LoadedDocument;
import com.farumazula.tinyapi.repository.DocumentRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Arrays;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class DocumentLoaderService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ResourcePatternResolver resolver;

    @Autowired
    private VectorStore vectorStore;

    @SneakyThrows
    @PostConstruct
    public void loadResources() {
        log.info("Loading resources");
        var resources = Arrays.asList(resolver.getResources("classpath:/knowlagebase/**/*"));
        resources.stream()
                .map(resource -> Pair.of(resource, getContentHash(resource)))
                .filter(pair -> !documentRepository.existsByFilenameAndContentHash(
                        pair.getFirst().getFilename(),
                        pair.getSecond()
                ).blockOptional().orElse(false))
                .forEach(pair -> {
                    var resource = pair.getFirst();
                    var contentHash = pair.getSecond();
                    var documents = new TextReader(resource).get();
                    var tokenTextSplitter = TokenTextSplitter.builder()
                            .withChunkSize(10_000)
                            .build();
                    var chunks = tokenTextSplitter.apply(documents);
                    vectorStore.accept(chunks);

                    var loadedDocument = LoadedDocument.builder()
                            .filename(resource.getFilename())
                            .chunkCount(chunks.size())
                            .contentHash(contentHash)
                            .documentType("txt")
                            .build();
                    log.info("Saving document {}", loadedDocument);
                    documentRepository.save(loadedDocument).block();
                });
    }

    @SneakyThrows
    private String getContentHash(Resource resource) {
        return DigestUtils.md5DigestAsHex(resource.getInputStream());
    }
}
