package ro.crownstudio.api.controller;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.crownstudio.api.db.entities.Log;
import ro.crownstudio.api.db.entities.SuiteRun;
import ro.crownstudio.api.db.entities.Test;
import ro.crownstudio.api.db.entities.TestRun;
import ro.crownstudio.api.db.repositories.LogRepository;
import ro.crownstudio.api.db.repositories.SuiteRunRepository;
import ro.crownstudio.api.db.repositories.TestRepository;
import ro.crownstudio.api.db.repositories.TestRunRepository;
import ro.crownstudio.api.enums.Status;
import ro.crownstudio.api.exceptions.InvalidInputDataException;
import ro.crownstudio.api.exceptions.ItemNotFoundException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TestRunController {

    private final TestRunRepository testRunRepository;
    private final SuiteRunRepository suiteRunRepository;
    private final LogRepository logRepository;
    private final TestRepository testRepository;

    @Autowired
    public TestRunController(
            TestRunRepository testRunRepository,
            SuiteRunRepository suiteRunRepository,
            LogRepository logRepository,
            TestRepository testRepository
    ) {
        this.testRunRepository = testRunRepository;
        this.suiteRunRepository = suiteRunRepository;
        this.logRepository = logRepository;
        this.testRepository = testRepository;
    }

    @GetMapping("/testruns")
    public ResponseEntity<List<TestRun>> getAllTestRuns() {
        List<TestRun> testRuns = testRunRepository.findAll();
        if (testRuns.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(testRuns);
    }

    @GetMapping("/testrun/{id}")
    public ResponseEntity<TestRun> getTestRunById(@PathVariable Long id) {
        TestRun testRun = testRunRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Test run with id %s was not found".formatted(id)));
        return ResponseEntity.ok(testRun);
    }

    @PostMapping("/testrun")
    @Transactional
    public ResponseEntity<TestRun> createTestRun(@RequestBody TestRunRequest testRunRequest) {
        if (testRunRequest.getTestId() == null) {
            throw new InvalidInputDataException("Missing test id for the test run.");
        }
        if (testRunRequest.getSuiteRunId() == null) {
            throw new InvalidInputDataException("Missing suite run id for the test run.");
        }

        Long testId = testRunRequest.getTestId();
        Long suiteRunId = testRunRequest.getSuiteRunId();

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ItemNotFoundException("Test with id %s was not found".formatted(testId)));
        SuiteRun suiteRun = suiteRunRepository.findById(suiteRunId)
                .orElseThrow(() -> new ItemNotFoundException("Suite Run with id %s was not found".formatted(suiteRunId)));

        TestRun testRun = new TestRun();
        testRun.setTest(test);
        testRun.setSuiteRun(suiteRun);
        testRun.setLogs(List.of());
        testRun.setStatus(Status.STARTED);
        testRun.setStartDate(Date.from(Instant.now()));

        TestRun createdTestRun = testRunRepository.save(testRun);

        suiteRun.getTestRuns().add(createdTestRun);

        suiteRunRepository.save(suiteRun);

        return ResponseEntity.ok(createdTestRun);
    }

    @PutMapping("/testrun/{id}")
    public ResponseEntity<TestRun> updateTestRunById(@PathVariable Long id, @RequestBody TestRun testRunRequest) {
        Optional<TestRun> optionalTestRun = testRunRepository.findById(id);
        if (optionalTestRun.isEmpty()) {
            throw new ItemNotFoundException("Test run with id %s was not found".formatted(id));
        }

        TestRun testRun = optionalTestRun.get();
        testRun.updateIfNotNull(testRunRequest);

        if (testRunRequest.getStatus().isEnding()) {
            testRun.setEndDate(Date.from(Instant.now()));
        }

        TestRun updatedTestRun = testRunRepository.save(testRun);
        return ResponseEntity.ok(updatedTestRun);
    }

    @DeleteMapping("/testrun/{id}")
    public ResponseEntity<Void> deleteTestRunById(@PathVariable Long id) {
        if (!testRunRepository.existsById(id)) {
            throw new ItemNotFoundException("Test run with id %s was not found".formatted(id));
        }

        testRunRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/testrun/{testRunId}/logs")
    public ResponseEntity<List<Log>> getLogsByTestRunId(@PathVariable Long testRunId) {
        TestRun testRun = testRunRepository.findById(testRunId)
                .orElseThrow(() -> new ItemNotFoundException("Test run with id %s was not found".formatted(testRunId)));

        List<Log> logs = logRepository.findByTestRun(testRun);
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logs);
    }

    @Getter
    private static class TestRunRequest {
        private Long testId;
        private Long suiteRunId;
    }
}
