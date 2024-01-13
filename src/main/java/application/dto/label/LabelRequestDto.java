package application.dto.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LabelRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
}
