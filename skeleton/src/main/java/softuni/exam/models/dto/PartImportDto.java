package softuni.exam.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class PartImportDto {

    @Size(min = 2, max = 19)
    @NotNull
    private String partName;

    @Min(10)
    @Max(2000)
    @NotNull
    private Double price;

    @Positive
    @NotNull
    private Integer quantity;
}
