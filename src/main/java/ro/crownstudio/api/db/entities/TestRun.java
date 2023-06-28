package ro.crownstudio.api.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ro.crownstudio.api.enums.Status;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "test_run")
@Getter
@Setter
public class TestRun extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "suite_run_id")
    @JsonIgnore
    private SuiteRun suiteRun;

    @OneToMany(mappedBy = "testRun")
    private List<Log> logs;

    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
}
