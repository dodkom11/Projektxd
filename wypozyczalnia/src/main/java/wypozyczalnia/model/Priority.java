package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL)
    private List<Task> task = new ArrayList<>();

    public Priority() {
    }

    public Priority(String name) {
        this.name = name;
    }
}
