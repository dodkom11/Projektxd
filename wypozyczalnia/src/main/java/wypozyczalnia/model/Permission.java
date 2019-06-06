package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<Account> account = new ArrayList<>();

    public Permission() {
    }

    public Permission(String name) {
        this.name = name;
    }
}
