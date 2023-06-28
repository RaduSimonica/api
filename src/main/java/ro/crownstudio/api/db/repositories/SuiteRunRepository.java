package ro.crownstudio.api.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.crownstudio.api.db.entities.SuiteRun;

public interface SuiteRunRepository extends JpaRepository<SuiteRun, Long> {}
