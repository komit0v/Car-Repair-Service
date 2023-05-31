package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.CarType;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "car_type",nullable = false)
    private CarType carType;

    @Column(name = "car_make", nullable = false)
    private String carMake;

    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column(nullable = false)
    private Integer year;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    @Column(nullable = false)
    private Integer kilometers;

    @Column(nullable = false)
    private Double engine;
}
