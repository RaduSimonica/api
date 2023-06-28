package ro.crownstudio.api.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.crownstudio.api.db.entities.Log;
import ro.crownstudio.api.db.entities.TestRun;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByTestRun(TestRun test);
}
