package application.service;

import application.dto.health.HealthResponseDto;

public interface HealthService {
    HealthResponseDto checkHealth();
}
