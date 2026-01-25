package com.vr.browser.service.registry.controller;

import com.vr.browser.service.registry.exception.RegistrationException;
import com.vr.browser.service.registry.request.BrowserRequest;
import com.vr.browser.service.registry.request.HeartBeatRequest;
import com.vr.browser.service.registry.request.RegisterRequest;
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

    @PostMapping("heart-beat")
    @Operation(description = "Register the browser session")
    public ResponseEntity<?> hearBeat(@RequestBody HeartBeatRequest heartBeatRequest) {
        try {
            log.info("Heart beat received from the service : {}", heartBeatRequest);
            return ResponseEntity.ok(browserRegistryService.heartBeat(heartBeatRequest));
        } catch (RegistrationException e) {
            log.error("Exception while listening to hear-beat of the service with id : {}", heartBeatRequest, e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("sessions")
    @Operation(description = "List all the browser sessions")
    public ResponseEntity<?> getRegisteredServices() {
        try {
            return ResponseEntity.ok(browserRegistryService.getRegisteredServices());
        } catch (RegistrationException e) {
            log.error("Exception while getting all registered service", e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("sessions")
    @Operation(description = "Request for new browser session")
    public ResponseEntity<?> requestBrowserSession(@RequestBody BrowserRequest browserRequest) {
        try {
            return ResponseEntity.ok(browserRegistryService.requestBrowserSession(browserRequest));
        } catch (RegistrationException e) {
            log.error("Exception while getting all registered service", e);
            return ResponseEntity.badRequest().body(e);
        }
    }
}
