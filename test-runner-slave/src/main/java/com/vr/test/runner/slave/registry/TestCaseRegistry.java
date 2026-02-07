package com.vr.test.runner.slave.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.test.runner.slave.exceptions.*;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.request.TestCaseStatusWrapper;
import com.vr.test.runner.slave.request.enums.TestCaseStatus;
import com.vr.test.runner.slave.response.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class TestCaseRegistry {
    public static final String NEW_TEST_CASE_IDS = "new-test-case-ids";
    public static final String FAILED_TEST_CASE_IDS = "failed-test-case-ids";
    public static final String PASSED_TEST_CASE_IDS = "passed-test-case-ids";
    public static final String RUNNING_TEST_CASE_IDS = "running-test-case-ids";
    public static final String TEST_CASE = "test-case:";
    public static final String TEST_RESULT = "test-result:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public TestCaseRegistry(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public String register(TestCase testCase) {

        try {
            String testCaseId = UUID.randomUUID().toString();
            testCase.setTestCaseId(testCaseId);
            //registering a test case with id
            redisTemplate.opsForValue().set(TEST_CASE + testCaseId, objectMapper.writeValueAsString(new TestCaseStatusWrapper(testCase, TestCaseStatus.NEW)));
            //registering new test case ids
            redisTemplate.opsForSet().add(NEW_TEST_CASE_IDS, testCaseId);
            log.debug("Registered test case {} with id {}", testCase, testCaseId);
            return testCaseId;
        } catch (JsonProcessingException e) {
            log.error("Error while registering test case {}", testCase);
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
        //removing a test case from running
        redisTemplate.opsForSet().remove(RUNNING_TEST_CASE_IDS, testCaseId);
        //marking a test case as failed
        redisTemplate.opsForSet().add(PASSED_TEST_CASE_IDS, testCaseId);
        //updating test case status to pass
        updateStatus(testCaseId, TestCaseStatus.PASSED);
    }

    public void setTestCaseToRunning(String testCaseId) {
        //removing a test case from new
        redisTemplate.opsForSet().remove(NEW_TEST_CASE_IDS, testCaseId);
        //marking a test case as running
        redisTemplate.opsForSet().add(RUNNING_TEST_CASE_IDS, testCaseId);
        //updating test case status to pass
        updateStatus(testCaseId, TestCaseStatus.RUNNING);
    }

    private void updateStatus(String testCaseId, TestCaseStatus testCaseStatus) {
        String testCaseStatusString = redisTemplate.opsForValue().get(TEST_CASE + testCaseId);
        try {
            TestCaseStatusWrapper testCaseStatusWrapper = objectMapper.readValue(testCaseStatusString, TestCaseStatusWrapper.class);
            testCaseStatusWrapper.setTestCaseStatus(testCaseStatus);
            redisTemplate.opsForValue().set(TEST_CASE + testCaseId, objectMapper.writeValueAsString(testCaseStatusWrapper));
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

    public Set<String> getRunningTestCaseIds() {
        return redisTemplate.opsForSet().members(RUNNING_TEST_CASE_IDS);
    }

    public Set<String> getPassedTestcaseIds() {
        return redisTemplate.opsForSet().members(PASSED_TEST_CASE_IDS);
    }

    public TestCase getTestCase(String testCaseId) {
        try {
            TestCaseStatusWrapper testCaseStatusWrapper = objectMapper.readValue(
                    redisTemplate.opsForValue().get(TEST_CASE + testCaseId),
                    TestCaseStatusWrapper.class
            );
            return testCaseStatusWrapper.getTestCase();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTestResult(String testCaseId, TestResult testResult) {
        try {
            redisTemplate.opsForValue().set(TEST_RESULT + testCaseId, objectMapper.writeValueAsString(testResult));
        } catch (JsonProcessingException e) {
            throw new TestResultUpdateException("Exception while updating the test result for test id: " + testResult, e);
        }
    }

    public TestResult getCaseResult(String testCaseId) {
        try {
            String testResultJson = redisTemplate.opsForValue().get(TEST_RESULT + testCaseId);
            if (Objects.isNull(testResultJson))
                throw new NoSuchTestResultException("Unable to find the test result with id : " + testCaseId);
            return objectMapper.readValue(testResultJson, TestResult.class);
        } catch (JsonProcessingException e) {
            throw new TestResultRetrievalException("Exception while retrieving the test result for test id: ");
        }
    }
}
