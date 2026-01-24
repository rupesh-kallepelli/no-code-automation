package com.vr.browser.service.registry.controller;

import com.vr.browser.service.registry.exception.RegistrationException;
import com.vr.browser.service.registry.request.RegisterRequest;
import com.vr.browser.service.registry.response.HeartBeatResponse;
import com.vr.browser.service.registry.response.RegistryResponse;
import com.vr.browser.service.registry.service.BrowserRegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1")
@Tag(name = "Browser Service Registry Controller",
        description = "This controller is used for registering the browser service and listening to the heartbeats of the browser"
)
public class BrowserServiceRegistryController {
    private final BrowserRegistryService browserRegistryService;

    public BrowserServiceRegistryController(BrowserRegistryService browserRegistryService) {
        this.browserRegistryService = browserRegistryService;
    }

    @PostMapping("register")
    @Operation(description = "Register the browser session")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            log.info("Registering the browser service with request : {}", registerRequest);
            RegistryResponse register = browserRegistryService.register(registerRequest);
            return ResponseEntity.ok(register);
        } catch (RegistrationException e) {
            log.error("Exception while registering the service with request: {}", registerRequest, e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("heart-beat/{id}")
    @Operation(description = "Register the browser session")
    public ResponseEntity<?> hearBeat(@PathVariable("id") String id) {
        try {
            log.info("Heart beat received from the service : {}", id);
            return ResponseEntity.ok(new HeartBeatResponse());
        } catch (RegistrationException e) {
            log.error("Exception while listening to hear-beat of the service with id : {}", id, e);
            return ResponseEntity.badRequest().body(e);
        }
    }
}
