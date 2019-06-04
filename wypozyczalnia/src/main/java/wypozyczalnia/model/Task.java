package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String status;
    private String priority;
    @JoinColumn(name = "group_id")
    @ManyToOne
    private Group group;
    @JoinColumn(name = "assigned_person")
    @OneToOne
    private Account account;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    public Task() {
    }

    public Task(String content, String status, String priority, Group group, Account account) {
        this.content = content;
        this.status = status;
        this.priority = priority;
        this.group = group;
        this.account = account;
    }
}
