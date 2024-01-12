package application.controller;

import application.dto.health.HealthResponseDto;
import application.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthController {
    private final HealthService healthService;

    @GetMapping
    @Operation(summary = "Get health info", description = "Informs that the application is working")
    public HealthResponseDto healthCheck() {
        return healthService.checkHealth();
    }
}
