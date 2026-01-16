package com.vr.browser.service.controller;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.service.BrowserFactory;
import com.vr.browser.service.service.BrowserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class BrowserController {
    private final BrowserFactory browserFactory;

    public BrowserController(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody @Valid BrowserRequest browserRequest) {

        log.debug("Received browser request : {}", browserRequest);
        try {
            BrowserService browser = browserFactory.getBrowser(browserRequest.browserType());
            BrowserSessionResponse browserSessionResponse = browser.launchBrowser(browserRequest);
            log.debug("Started the browser successfully with websocket url : {}", browserSessionResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(browserSessionResponse);
        } catch (Exception e) {
            log.error("Couldn't start the browser session", e);
            return ResponseEntity.internalServerError()
                    .body("Couldn't start the browser session : " + e.getMessage());
        }

    }

    @DeleteMapping("/close")
    public ResponseEntity<?> closeSession(@RequestParam Long id) {
        BrowserService.killBrowserProcess(id);
        return ResponseEntity.ok("Closed the session with id : " + id);
    }
}
