package ro.crownstudio.api.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "log")
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_run_id")
    @JsonIgnore
    private TestRun testRun;

    private String logLevel;

    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
