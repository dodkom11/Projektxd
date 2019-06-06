package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Bracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "leader")
    private Long leader;
    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL)
    private List<Account> account = new ArrayList<>();
    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL)
    private List<Task> task = new ArrayList<>();

    public Bracket() {
    }

    public Bracket(Long leader) {
        this.leader = leader;
    }
}
