package application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.mapper.HealthMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class HealthServiceImplTest {
    private static final String STATUS_SUCCESS = "Success!";
    @InjectMocks
    private HealthServiceImpl healthService;
    @Mock
    private HealthMapper healthMapper;

    @Test
    public void checkHealth_PositiveFlow_ReturnStatusOk() {
        assertEquals(
                healthMapper.toDto(STATUS_SUCCESS, HttpStatus.OK),
                healthService.checkHealth()
        );
    }
}
