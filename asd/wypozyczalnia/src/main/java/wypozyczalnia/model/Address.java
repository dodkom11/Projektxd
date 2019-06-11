package wypozyczalnia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String pesel;
    private String city;
    private String street;
    @Column(name = "house_number")
    private Integer houseNumber;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "telephone_number")
    private Long telephoneNumber;
    private String email;
    @OneToOne(mappedBy = "address", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Client client;
    @OneToOne(mappedBy = "address", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Employee  employee;

    public Address() {
    }

    public Address(String name, String surname, String pesel, String city, String street, int houseNumber, String zipCode,
                   Long telephoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }
}
