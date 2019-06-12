package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@Data
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "employee_id")
    @ManyToOne
    private Employee employee;
    @JoinColumn(name = "car_id")
    @ManyToOne
    private Car car;
    @JoinColumn(name = "client_id")
    @ManyToOne
    private Client client;
    @Column(name = "rental_time")
    private int rentalTime;
    @Column(name = "rental_date")
    private Date rentalDate;


    public Rent() {
    }


    public Rent(Employee employee, Car car, Client client, int rentalTime, Date rentalDate) {
        this.employee = employee;
        this.car = car;
        this.client = client;
        this.rentalTime = rentalTime;
        this.rentalDate = rentalDate;
    }
}
