package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "leader")
    private Long leader;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Account> account = new ArrayList<>();
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Task> task = new ArrayList<>();

    public Group() {
    }

    public Group(Long leader) {
        this.leader = leader;
    }
}
