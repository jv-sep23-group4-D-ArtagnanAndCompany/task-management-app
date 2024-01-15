package application.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TelegramBotConfig {
    @Value("${TELEGRAM_BOT_NAME}")
    private String botName;
    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;
}
