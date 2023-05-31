package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskImportDto {

    @XmlElement(name = "date")
    private String date;

    @XmlElement
    @Positive
    @NotNull
    private BigDecimal price;

    @XmlElement(name = "car")
    private CarIdOnlyDto car;

    @XmlElement(name = "mechanic")
    private MechanicFirstNameOnlyDto mechanic;

    @XmlElement(name = "part")
    private PartIdOnlyDto part;

}
