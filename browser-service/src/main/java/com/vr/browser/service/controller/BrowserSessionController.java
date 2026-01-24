package com.vr.browser.service.controller;

import com.vr.browser.service.registry.BrowserRegistry;
import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.response.SessionDeleteResponse;
import com.vr.browser.service.service.BrowserFactory;
import com.vr.browser.service.service.BrowserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Slf4j
@Tag(name = "Browser Session Controller",
        description = "Browser Session Controller is used to create, delete, delete all, list all the browser instances which are created"
)
public class BrowserSessionController {
    private final BrowserFactory browserFactory;
    private final BrowserRegistry browserRegistry;

    public BrowserSessionController(
            BrowserFactory browserFactory,
            BrowserRegistry browserRegistry
    ) {
        this.browserFactory = browserFactory;
        this.browserRegistry = browserRegistry;
    }


    @PostMapping("sessions")
    @Operation(description = "Creating the browser instances")
    public ResponseEntity<?> createSession(@RequestBody @Valid BrowserRequest browserRequest) {
        log.debug("Received browser request : {}", browserRequest);
        try {
            BrowserService browserService = browserFactory.getBrowserService(browserRequest.browserType());
            BrowserSessionResponse browserSessionResponse = browserService.launchBrowser(browserRequest);
            log.debug("Started the browser successfully with websocket url : {}", browserSessionResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(browserSessionResponse);
        } catch (Exception e) {
            log.error("Couldn't start the browser session", e);
            return ResponseEntity.internalServerError().body("Couldn't start the browser session : " + e.getMessage());
        }
    }

    @DeleteMapping("sessions/{sessionId}")
    @Operation(description = "Killing the browser instances with give id")
    public ResponseEntity<?> closeSession(@PathVariable String sessionId) {
        browserRegistry.killBrowserProcess(sessionId);
        return ResponseEntity.ok(new SessionDeleteResponse(sessionId, "terminated"));
    }

    @DeleteMapping("sessions/all")
    @Operation(description = "Killing all the browser instances opened")
    public ResponseEntity<?> closeSessions() {
        browserRegistry.killAll();
        return ResponseEntity.ok("Closed all Sessions");
    }

    @GetMapping("sessions")
    @Operation(description = "List all the created browser instances")
    public ResponseEntity<?> sessions() {
        return ResponseEntity.ok(browserRegistry.getAllBrowserSessions());
    }

}
