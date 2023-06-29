package ro.crownstudio.api.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.crownstudio.api.db.entities.Test;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByInternalId(String internalId);
}
