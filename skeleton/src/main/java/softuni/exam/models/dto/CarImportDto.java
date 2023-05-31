package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.CarType;

import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarImportDto {

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String carMake;

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String carModel;

    @Positive
    @NotNull
    @XmlElement
    private Integer year;

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String plateNumber;

    @Positive
    @NotNull
    @XmlElement
    private Integer kilometers;

    @DecimalMin("1.00")
    @NotNull
    @XmlElement
    private Double engine;

    @Enumerated
    @NotNull
    @XmlElement
    private CarType carType;
}
