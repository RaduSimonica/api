package ro.crownstudio.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.crownstudio.api.db.entities.Test;
import ro.crownstudio.api.db.repositories.TestRepository;
import ro.crownstudio.api.exceptions.InvalidInputDataException;
import ro.crownstudio.api.exceptions.ItemNotFoundException;

import java.util.List;

@RestController
public class TestController {
    private final TestRepository testRepository;

    @Autowired
    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/tests")
    public ResponseEntity<List<Test>> getAllTests() {
        List<Test> tests = testRepository.findAll();
        if (tests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Test with id %s was not found".formatted(id)));
        return ResponseEntity.ok(test);
    }

    @GetMapping("/test")
    public ResponseEntity<Test> getTestByInternalId(@RequestParam("internalId") String internalId) {
        Test test = testRepository.findByInternalId(internalId)
                .orElseThrow(() -> new ItemNotFoundException("Test with internal id %s was not found".formatted(internalId)));

        return ResponseEntity.ok(test);
    }

    @PostMapping("/test")
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        if (test.getName() == null || test.getName().isEmpty()) {
            throw new InvalidInputDataException("Missing test name.");
        }

        Test savedTest = testRepository.save(test);
        return ResponseEntity.ok(savedTest);
    }

    @PutMapping("/test/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Long id, @RequestBody Test testRequest) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Test with id %s was not found".formatted(id)));

        test.updateIfNotNull(testRequest);

        Test updatedTest = testRepository.save(test);
        return ResponseEntity.ok(updatedTest);
    }

    @DeleteMapping("/test/{id}")
    public ResponseEntity<Void> deleteTestById(@PathVariable Long id) {
        if (!testRepository.existsById(id)) {
            throw new ItemNotFoundException("Test with id %s was not found".formatted(id));
        }

        testRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
