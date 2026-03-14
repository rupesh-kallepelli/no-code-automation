package com.vr.ai.test.planner.service.impl;

import com.vr.ai.test.planner.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final VectorStore vectorStore;

    @Override
    @Async
    public void embedFile(MultipartFile multipartFile) {
        CompletableFuture.runAsync(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }

                lines.parallelStream().forEach(l -> {
                    vectorStore.add(List.of(new Document(l)));
                    log.debug("Added line {}", l);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
