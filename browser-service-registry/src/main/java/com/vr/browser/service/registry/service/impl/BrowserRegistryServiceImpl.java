package com.vr.browser.service.registry.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.browser.service.registry.exception.*;
import com.vr.browser.service.registry.request.BrowserRequest;
import com.vr.browser.service.registry.request.HeartBeatRequest;
import com.vr.browser.service.registry.request.RegisterRequest;
import com.vr.browser.service.registry.response.BrowserSessionResponse;
import com.vr.browser.service.registry.response.HeartBeatResponse;
import com.vr.browser.service.registry.response.RegistryResponse;
import com.vr.browser.service.registry.response.SessionDeleteResponse;
import com.vr.browser.service.registry.service.BrowserRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.vr.browser.service.registry.constants.Constants.*;

@Slf4j
@Service
public class BrowserRegistryServiceImpl implements BrowserRegistryService {

    public static final String BROWSER_SESSION_DETAILS = "browser-session-details:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final int thresholdLimit;
    private final String registryHost;
    private final int port;

    public BrowserRegistryServiceImpl(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper,
            @Value("${max.session.threshold}") int thresholdLimit,
            @Value("${registry.host}") String registryHost,
            @Value("${server.port}") int port
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.thresholdLimit = thresholdLimit;
        this.registryHost = registryHost;
        this.port = port;
    }

    @Override
    public RegistryResponse register(RegisterRequest registerRequest) {
        try {
            String registrationId = BROWSER_SERVICE + UUID.randomUUID();
            //caching registration requests to redis
            redisTemplate.opsForValue().set(registrationId, objectMapper.writeValueAsString(registerRequest));
            //registering the registration id to redis
            redisTemplate.opsForSet().add(BROWSER_SERVICE_IDS, registrationId);

            return new RegistryResponse(registrationId, "CONNECTED", "200");
        } catch (Exception e) {
            log.error("Exception while registering the request", e);
            throw new RegistrationException("Exception while registering the request", e);
        }
    }

    @Override
    public HeartBeatResponse heartBeat(HeartBeatRequest heartBeatRequest) {

        String heartBeatTrackerId = HEART_BEAT + BROWSER_SERVICE + heartBeatRequest.id();
        //updating the heartbeat to redis
        redisTemplate.opsForValue().set(heartBeatTrackerId, System.currentTimeMillis() + ":" + heartBeatRequest.activeSessionCount());

        return new HeartBeatResponse();
    }

    @Override
    public Set<String> getRegisteredServices() {
        return Objects.requireNonNull(redisTemplate.opsForSet().members(BROWSER_SERVICE_IDS));
    }

    @Override
    public Mono<BrowserSessionResponse> requestBrowserSession(BrowserRequest browserRequest) {
        RegisterRequest healthyService = findHealthyService(browserRequest);

        log.info("Healthy service : " + healthyService);

        //requesting for new session from healthy service
        return WebClient.builder()
                .baseUrl("http://" + healthyService.ipAddress() + ":" + healthyService.port()).build()
                .post()
                .uri(BROWSER_SERVICE_SESSION_PATH)
                .bodyValue(browserRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new ClientSideException("Couldn't make request to browser service due to client side exception" + clientResponse))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new ServerSideException("Couldn't make request to browser service due to server side exception" + clientResponse))
                )
                .bodyToMono(BrowserSessionResponse.class)
                .map(browserSessionResponse -> {
                    try {
                        //fetching websocket url of healthy browser service
                        URI sessionUrl = new URI(browserSessionResponse.getWsUrl());

                        //registering the browser service ws url
                        redisTemplate.opsForValue().set(BROWSER_SERVICE_WS + browserSessionResponse.getSessionId(), sessionUrl.toString());
                        //registering the browser session details
                        redisTemplate.opsForValue().set(BROWSER_SESSION_DETAILS + browserSessionResponse.getSessionId(), objectMapper.writeValueAsString(browserSessionResponse));

                        URI newSocketUrl = getNewSocketUrl(sessionUrl);

                        browserSessionResponse.setWsUrl(newSocketUrl.toString());

                        return browserSessionResponse;
                    } catch (URISyntaxException | JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Mono<SessionDeleteResponse> killBrowserSession(String sessionId) {
        try {
            //fetching browser session details
            String browserSessionDetails = redisTemplate.opsForValue().get(BROWSER_SESSION_DETAILS + sessionId);
            BrowserSessionResponse browserSessionResponse = objectMapper.readValue(browserSessionDetails, BrowserSessionResponse.class);
            return WebClient.builder()
                    .baseUrl("http://" + browserSessionResponse.getAddress() + ":" + browserSessionResponse.getPort()).build()
                    .delete()
                    .uri(BROWSER_SERVICE_SESSION_PATH + "/" + sessionId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new ClientSideException("Couldn't make request to browser service for killing session %s due to client side exception : ".formatted(sessionId)))
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> Mono.error(new ServerSideException("Couldn't make request to browser service for killing session %s due to server side exception : ".formatted(sessionId) + clientResponse))
                    ).bodyToMono(SessionDeleteResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private URI getNewSocketUrl(URI sessionUrl) throws URISyntaxException {
        String scheme = sessionUrl.getScheme();
        if ("ws".equalsIgnoreCase(scheme)) {
            scheme = "wss";
        }

        //mapping the session to registry service url
        return new URI(
                scheme,
                sessionUrl.getUserInfo(),
                registryHost,
                registryHost.equals(LOCALHOST) ? port : -1,
                sessionUrl.getPath(),
                sessionUrl.getQuery(),
                sessionUrl.getFragment()
        );
    }

    private RegisterRequest findHealthyService(BrowserRequest browserRequest) {
        // Get all registration IDs from the set
        Set<String> registrationIds = redisTemplate.opsForSet().members(BROWSER_SERVICE_IDS);

        if (registrationIds == null || registrationIds.isEmpty())
            throw new NoActiveSessionsException("No Active sessions currently registered");

        return registrationIds
                .stream()
                //filtering on services has active sessions less than a threshold
                .filter(registrationId -> {
                    String heartBeatTrackerKey = HEART_BEAT + BROWSER_SERVICE + registrationId;
                    String lastHeartBeat = redisTemplate.opsForValue().get(heartBeatTrackerKey);

                    if (lastHeartBeat == null) return false;

                    String[] heartBeatArgs = lastHeartBeat.split(":");
                    int activeSessionCount = Integer.parseInt(heartBeatArgs[1]);

                    return activeSessionCount < thresholdLimit;
                })
                //mapping to registration request
                .map(healthySessionId -> {
                    try {
                        return objectMapper.readValue(redisTemplate.opsForValue().get(healthySessionId), RegisterRequest.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                //filtering on a browser type
                .filter(registerRequest -> registerRequest.browserType().equals(browserRequest.browserType()))
                .findFirst()
                .orElseThrow(() -> new NoHealthyServiceFoundException("Not active session found, please try again later"));
    }

    @Scheduled(fixedRate = 5000)
    public void cleanUp() {
        // Get all registration IDs from the set
        Set<String> registrationIds = redisTemplate.opsForSet().members(BROWSER_SERVICE_IDS);
        if (registrationIds == null || registrationIds.isEmpty()) return;

        registrationIds.forEach(registrationId -> {
            String heartBeatTrackerKey = HEART_BEAT + BROWSER_SERVICE + registrationId;

            String lastHeartBeat = redisTemplate.opsForValue().get(heartBeatTrackerKey);
            if (lastHeartBeat == null) {//heart beats might not have received, need eviction from the browser-service-ids
                redisTemplate.opsForSet().remove(BROWSER_SERVICE_IDS, registrationId);
                return;
            }

            String[] heartBeatArgs = lastHeartBeat.split(":");
            long timestamp = Long.parseLong(heartBeatArgs[0]);

            // If heartbeat is too old
            if (System.currentTimeMillis() - timestamp > 10000) {
                // Remove the heartbeat key
                redisTemplate.delete(heartBeatTrackerKey);

                // Remove from registration IDs set
                redisTemplate.opsForSet().remove(BROWSER_SERVICE_IDS, registrationId);

                // Remove the actual registration info
                String registrationKey = BROWSER_SERVICE + registrationId;
                redisTemplate.delete(registrationKey);

                log.debug("Evicted stale registration: {}", registrationId);
            }
        });
    }
}

