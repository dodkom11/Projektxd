package wypozyczalnia.config;

import java.time.LocalDate;

public class DisabledRange {

    private final LocalDate initialDate;
    private final LocalDate endDate;

    public DisabledRange(LocalDate initialDate, LocalDate endDate){
        this.initialDate=initialDate;
        this.endDate = endDate;
    }

    public LocalDate getInitialDate() { return initialDate; }
    public LocalDate getEndDate() { return endDate; }

}