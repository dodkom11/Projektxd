package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<Task> task = new ArrayList<>();

    public State() {
    }

    public State(String name) {
        this.name = name;
    }
}
