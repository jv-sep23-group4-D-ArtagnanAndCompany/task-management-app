package application.dto.label;

import application.dto.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LabelRequestDto {
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String name;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String color;
}
