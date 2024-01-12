package application.dto.health;

import org.springframework.http.HttpStatus;

public record HealthResponseDto(String response, HttpStatus httpStatus) {
}
