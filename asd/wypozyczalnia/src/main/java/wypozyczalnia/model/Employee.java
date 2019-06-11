package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnDefault("true")
    private Boolean active = true;
    private double salary;
    @Column(name = "employment_date")
    private Date employmentDate;
    private String position;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Rent> rent = new ArrayList<>();


    public Employee() {
    }

    public Employee(double salary, Date employmentDate, String position, Address address) {
        this.salary = salary;
        this.employmentDate = employmentDate;
        this.position = position;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}
