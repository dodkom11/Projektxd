package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Column(name = "production_year")
    private int productionYear;
    private String vin;
    private float capacity;
    private String type;
    private float price;


    public Car() {
    }

    public Car(String brand, String model, int productionYear, String vin, float capacity, String type, float price) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.vin = vin;
        this.capacity = capacity;
        this.type = type;
        this.price = price;
    }
}
