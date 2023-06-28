package ro.crownstudio.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.crownstudio.api.db.entities.Log;
import ro.crownstudio.api.db.entities.SuiteRun;
import ro.crownstudio.api.db.entities.TestRun;
import ro.crownstudio.api.db.repositories.LogRepository;
import ro.crownstudio.api.db.repositories.SuiteRunRepository;
import ro.crownstudio.api.db.repositories.TestRunRepository;
import ro.crownstudio.api.exceptions.ItemNotFoundException;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@RestController
public class LogController {
    private final LogRepository logRepository;
    private final TestRunRepository testRunRepository;
    private final SuiteRunRepository suiteRunRepository;

    @Autowired
    public LogController(
            LogRepository logRepository,
            TestRunRepository testRunRepository,
            SuiteRunRepository suiteRunRepository
    ) {
        this.logRepository = logRepository;
        this.testRunRepository = testRunRepository;
        this.suiteRunRepository = suiteRunRepository;
    }

    @GetMapping("/suiterun/{suiteRunId}/testrun/{testRunId}/logs")
    public ResponseEntity<List<Log>> getLogsByTestRunId(@PathVariable Long suiteRunId, @PathVariable Long testRunId) {
        SuiteRun suiteRun = suiteRunRepository.findById(suiteRunId)
                .orElseThrow(() -> new ItemNotFoundException("Suite run with id %s was not found".formatted(suiteRunId)));

        TestRun testRun = suiteRun.getTestRuns().stream()
                .filter(x -> x.getId().equals(testRunId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Test run with id %s was not found".formatted(testRunId)));

        List<Log> logs = logRepository.findByTestRun(testRun);
        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/testrun/{id}/log")
    public ResponseEntity<Log> createLog(@PathVariable Long id, @RequestBody Log logRequest) {
        TestRun testRun = testRunRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Test with id %s was not found".formatted(id)));

        logRequest.setTestRun(testRun);
        logRequest.setTimestamp(Date.from(Instant.now()));

        Log createdLog = logRepository.save(logRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    @DeleteMapping("/log/{id}")
    public ResponseEntity<Void> deleteLogById(@PathVariable Long id) {
        if (!logRepository.existsById(id)) {
            throw new ItemNotFoundException("Log with id %s was not found".formatted(id));
        }

        logRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

