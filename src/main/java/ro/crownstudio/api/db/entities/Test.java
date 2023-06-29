package ro.crownstudio.api.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test")
@Getter
@Setter
public class Test extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String internalId;

    private String name;

    private String description;
}
