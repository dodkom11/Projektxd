package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;
    private String content;
    @JoinColumn(name = "task_id")
    @ManyToOne
    private Task task;

    public Comment() {
    }

    public Comment(Account account, String content, Task task) {
        this.account = account;
        this.content = content;
        this.task = task;
    }
}
