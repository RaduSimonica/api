package ro.crownstudio.api.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.crownstudio.api.db.entities.SuiteRun;
import ro.crownstudio.api.db.entities.TestRun;

import java.util.List;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    List<TestRun> findBySuiteRun(SuiteRun test);
}
