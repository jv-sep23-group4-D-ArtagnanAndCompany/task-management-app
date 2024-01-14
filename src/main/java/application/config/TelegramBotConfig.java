package application.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TelegramBotConfig {
    private String botName = "DA_task_manager_bot";
    private String token = "6922423500:AAERt55PLpi6pWgDN5KwrhTyd_oxOh_jesg";
}
