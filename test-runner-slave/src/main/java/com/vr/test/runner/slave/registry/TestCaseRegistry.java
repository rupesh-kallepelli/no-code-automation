package com.vr.test.runner.slave.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.test.runner.slave.exceptions.FailedToRegisterInCacheException;
import com.vr.test.runner.slave.exceptions.FailedToUpdateStatusException;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.request.TestCaseStatusWrapper;
import com.vr.test.runner.slave.request.enums.TestCaseStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class TestCaseRegistry {
    public static final String NEW_TEST_CASE_IDS = "new-test-case-ids";
    public static final String FAILED_TEST_CASE_IDS = "failed-test-case-ids";
    public static final String PASSED_TEST_CASE_IDS = "passed-test-case-ids";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public TestCaseRegistry(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public String register(TestCase testCase) {

        try {
            String testCaseId = UUID.randomUUID().toString();
            //registering a test case with id
            redisTemplate.opsForValue().set("test-case:" + testCaseId, objectMapper.writeValueAsString(new TestCaseStatusWrapper(testCase, TestCaseStatus.NEW)));
            //registering new test case ids
            redisTemplate.opsForSet().add(NEW_TEST_CASE_IDS, testCaseId);

            return testCaseId;
        } catch (JsonProcessingException e) {
            throw new FailedToRegisterInCacheException("Failed to Register the test in cache : ", e);
        }
    }

    public void setTestCaseToFailed(String testCaseId) {
        //removing a test case from new
        redisTemplate.opsForSet().remove(NEW_TEST_CASE_IDS, testCaseId);
        //marking a test case as failed
        redisTemplate.opsForSet().add(FAILED_TEST_CASE_IDS, testCaseId);
        //updating test case status to fail
        updateStatus(testCaseId, TestCaseStatus.FAILED);
    }

    public void setTestCaseToPassed(String testCaseId) {
        //removing a test case from new
        redisTemplate.opsForSet().remove(NEW_TEST_CASE_IDS, testCaseId);
        //marking a test case as failed
        redisTemplate.opsForSet().add(PASSED_TEST_CASE_IDS, testCaseId);
        //updating test case status to pass
        updateStatus(testCaseId, TestCaseStatus.PASSED);
    }

    public void setTestCaseToRunning(String testCaseId) {
        //removing a test case from new
        redisTemplate.opsForSet().remove(NEW_TEST_CASE_IDS, testCaseId);
        //marking a test case as failed
        redisTemplate.opsForSet().add("running-test-case-ids", testCaseId);
        //updating test case status to pass
        updateStatus(testCaseId, TestCaseStatus.PASSED);
    }

    private void updateStatus(String testCaseId, TestCaseStatus testCaseStatus) {
        String testCaseStatusString = redisTemplate.opsForValue().get("test-case:" + testCaseId);
        try {
            TestCaseStatusWrapper testCaseStatusWrapper = objectMapper.readValue(testCaseStatusString, TestCaseStatusWrapper.class);
            testCaseStatusWrapper.setTestCaseStatus(testCaseStatus);
            redisTemplate.opsForValue().set("test-case:" + testCaseId, objectMapper.writeValueAsString(testCaseStatusWrapper));
        } catch (JsonProcessingException e) {
            throw new FailedToUpdateStatusException("Exception while updating status ", e);
        }
    }

    public Set<String> getNewTestcaseIds() {
        return redisTemplate.opsForSet().members(NEW_TEST_CASE_IDS);
    }

    public Set<String> getFailedTestcaseIds() {
        return redisTemplate.opsForSet().members(FAILED_TEST_CASE_IDS);
    }

    public Set<String> getPassedTestcaseIds() {
        return redisTemplate.opsForSet().members(PASSED_TEST_CASE_IDS);
    }


    public TestCase getTestCase(String testCaseId) {
        try {
            TestCaseStatusWrapper testCaseStatusWrapper = objectMapper.readValue(redisTemplate.opsForValue().get(testCaseId), TestCaseStatusWrapper.class);
            return testCaseStatusWrapper.getTestCase();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
