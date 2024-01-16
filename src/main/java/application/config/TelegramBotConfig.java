package application.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TelegramBotConfig {
    @Value("${telegram_bot_name}")
    private String botName;
    @Value("${telegram_bot_token}")
    private String token;
}
