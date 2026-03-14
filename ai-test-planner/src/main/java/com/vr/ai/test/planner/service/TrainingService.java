package com.vr.ai.test.planner.service;

import org.springframework.web.multipart.MultipartFile;

public interface TrainingService {
    void embedFile(MultipartFile multipartFile);
}
