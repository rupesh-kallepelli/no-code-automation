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
        description = "This controller is used for registering the browser service," +
                " listening to the heartbeats of the browser," +
                " requesting browser session and deleting the opened session"
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
            log.debug("Registering the browser service with request : {}", registerRequest);
            RegistryResponse register = browserRegistryService.register(registerRequest);
            return ResponseEntity.ok(register);
        } catch (RegistrationException e) {
            log.error("Exception while registering the service with request: {}", registerRequest, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Exception while registering the service with request : {}", registerRequest, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("heart-beat")
    @Operation(description = "Register the browser session")
    public ResponseEntity<?> hearBeat(@RequestBody @Valid HeartBeatRequest heartBeatRequest) {
        try {
            log.debug("Heart beat received from the service : {}", heartBeatRequest);
            return ResponseEntity.ok(browserRegistryService.heartBeat(heartBeatRequest));
        } catch (Exception e) {
            log.error("Exception while listening to hear-beat of the service with id : {}", heartBeatRequest, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("sessions")
    @Operation(description = "List all the browser sessions")
    public ResponseEntity<?> getRegisteredServices() {
        try {
            return ResponseEntity.ok(browserRegistryService.getRegisteredServices());
        } catch (Exception e) {
            log.error("Exception while getting all registered service", e);
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("sessions")
    @Operation(description = "Request for new browser session")
    public ResponseEntity<?> requestBrowserSession(@RequestBody BrowserRequest browserRequest) {
        try {
            return ResponseEntity.ok(browserRegistryService.requestBrowserSession(browserRequest));
        } catch (Exception e) {
            log.error("Exception while getting all registered service", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("sessions/{sessionId}")
    @Operation(description = "Delete browser session")
    public ResponseEntity<?> killBrowserSession(@PathVariable("sessionId") String sessionId) {
        try {
            return ResponseEntity.ok(browserRegistryService.killBrowserSession(sessionId));
        } catch (Exception e) {
            log.error("Exception while getting all registered service", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
