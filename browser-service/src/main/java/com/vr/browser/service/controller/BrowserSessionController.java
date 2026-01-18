package com.vr.browser.service.controller;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.response.SessionDeleteResponse;
import com.vr.browser.service.service.BrowserFactory;
import com.vr.browser.service.service.BrowserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class BrowserSessionController {
    private final BrowserFactory browserFactory;

    public BrowserSessionController(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    @PostMapping("/sessions")
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

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<?> closeSession(@PathVariable String sessionId) {
        BrowserService.killBrowserProcess(sessionId);
        return ResponseEntity.ok(new SessionDeleteResponse(Long.valueOf(sessionId), "terminated"));
    }

    @DeleteMapping("/sessions/all")
    public ResponseEntity<?> closeSessions() {
        BrowserService.killAll();
        return ResponseEntity.ok("Closed all Sessions");
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> sessions() {
        Map<String, String> sessionMap = new HashMap<>();
        BrowserService.PROCESS_CACHE.forEach((aLong, browserDetails) ->
                sessionMap.put(String.valueOf(aLong), browserDetails.getAddress() + ":" + browserDetails.getPort())
        );
        return ResponseEntity.ok(sessionMap);
    }

}
