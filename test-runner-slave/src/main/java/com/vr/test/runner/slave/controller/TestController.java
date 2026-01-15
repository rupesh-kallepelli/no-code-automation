package com.vr.test.runner.slave.controller;

import com.vr.test.runner.slave.request.TestPlan;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class TestController {


    @PostMapping
    public ResponseEntity<?> runTest(@RequestBody @Valid TestPlan testPlan) {
        return ResponseEntity.ok().body("Test started");
    }

}
