package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    private Part part;

    @ManyToOne
    private Mechanic mechanic;

    @ManyToOne
    private Car car;

    @Override
    public String toString() {
        return
                "Car " + this.car.getCarMake() + " " + this.car.getCarModel() + " with " + this.car.getKilometers() + "km" + System.lineSeparator() +
                "-Mechanic: " + this.mechanic.getFirstName() + " " + this.mechanic.getLastName() + " - task №" + this.getId() + System.lineSeparator() +
                "--Engine: " + this.car.getEngine() + System.lineSeparator() +
                "---Price: " + this.getPrice() + "$";

    }
}
