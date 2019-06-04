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
    private String permissions;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Employee employee;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Task task;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();


    public Account() {
    }

    public Account(String username, String password, String permissions, Group group) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.group = group;
    }
}
