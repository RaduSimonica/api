package ro.crownstudio.api.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ro.crownstudio.api.enums.Status;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "suite_run")
@Getter
@Setter
public class SuiteRun extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "suiteRun", cascade = CascadeType.ALL)
    private List<TestRun> testRuns;

    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
}
