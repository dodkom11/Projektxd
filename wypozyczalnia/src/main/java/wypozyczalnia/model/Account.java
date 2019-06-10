package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
    @ManyToOne
    @JoinColumn(name = "bracket_id")
    private Bracket bracket;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Employee employee;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Task> task = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Comment> comment = new ArrayList<>();


    public Account() {
    }

    public Account(String username, String password, Permission permission, Bracket bracket) {
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.bracket = bracket;
    }
}
