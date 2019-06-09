package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByBrand(String brand);
    List<Car> findAllByType(String type);
    List<Car> findByPriceLessThanAndPriceGreaterThanAndCapacityLessThanAndCapacityGreaterThanAndProductionYearLessThanAndProductionYearGreaterThan(float priceFrom, float priceTo, float capacityFrom, float capacityTo, int yearFrom, int yearTo);
    List<Car> findAllByPriceLessThanAndPriceGreaterThan(int x, int y);
    List<Car> findAllByPriceAfter(int x);
}
