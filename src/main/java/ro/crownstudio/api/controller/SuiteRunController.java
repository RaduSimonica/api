package ro.crownstudio.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.crownstudio.api.db.entities.SuiteRun;
import ro.crownstudio.api.db.repositories.SuiteRunRepository;
import ro.crownstudio.api.enums.Status;
import ro.crownstudio.api.exceptions.ItemNotFoundException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class SuiteRunController {
    private final SuiteRunRepository suiteRunRepository;

    @Autowired
    public SuiteRunController(SuiteRunRepository suiteRunRepository) {
        this.suiteRunRepository = suiteRunRepository;
    }

    @GetMapping("/suiteruns")
    public ResponseEntity<List<SuiteRun>> getAllSuiteRuns() {
        List<SuiteRun> suiteRuns = suiteRunRepository.findAll();
        if (suiteRuns.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(suiteRuns);
    }

    @GetMapping("/suiterun/{id}")
    public ResponseEntity<SuiteRun> getSuiteRunById(@PathVariable Long id) {
        SuiteRun suiteRun = suiteRunRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Suite run with id %s was not found".formatted(id)));
        return ResponseEntity.ok(suiteRun);
    }

    @PostMapping("/suiterun")
    public ResponseEntity<SuiteRun> createSuiteRun() {
        SuiteRun suiteRun = new SuiteRun();
        suiteRun.setStatus(Status.STARTED);
        suiteRun.setStartDate(Date.from(Instant.now()));

        SuiteRun createdSuiteRun = suiteRunRepository.save(suiteRun);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSuiteRun);
    }

    @PutMapping("/suiterun/{id}")
    public ResponseEntity<SuiteRun> updateSuiteRun(@PathVariable Long id, @RequestBody SuiteRun suiteRunRequest) {
        Optional<SuiteRun> optionalSuiteRun = suiteRunRepository.findById(id);
        if (optionalSuiteRun.isEmpty()) {
            throw new ItemNotFoundException("Suite run with id %s was not found".formatted(id));
        }

        SuiteRun suiteRun = optionalSuiteRun.get();
        suiteRun.updateIfNotNull(suiteRunRequest);

        if (suiteRunRequest.getStatus().isEnding()) {
            suiteRun.setEndDate(Date.from(Instant.now()));
        }

        SuiteRun updatedSuiteRun = suiteRunRepository.save(suiteRun);
        return ResponseEntity.ok(updatedSuiteRun);
    }

    @DeleteMapping("/suiterun/{id}")
    public ResponseEntity<Void> deleteSuiteRunById(@PathVariable Long id) {
        if (!suiteRunRepository.existsById(id)) {
            throw new ItemNotFoundException("Suite run with id %s was not found".formatted(id));
        }

        suiteRunRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}