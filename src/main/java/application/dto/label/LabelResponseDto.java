package application.dto.label;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LabelResponseDto {
    private Long id;
    private String name;
    private String color;
}
