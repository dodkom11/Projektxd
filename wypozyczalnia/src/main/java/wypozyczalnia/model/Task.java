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
    private String title;
    private String content;
    @JoinColumn(name = "state_id")
    @ManyToOne
    private State state;
    @JoinColumn(name = "priority_id")
    @ManyToOne
    private Priority priority;
    @JoinColumn(name = "bracket_id")
    @ManyToOne
    private Bracket bracket;
    @JoinColumn(name = "assigned_person")
    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    public Task() {
    }

    public Task(String title, String content, State state, Priority priority, Bracket bracket, Account account) {
        this.title = title;
        this.content = content;
        this.state = state;
        this.priority = priority;
        this.bracket = bracket;
        this.account = account;
    }
}
