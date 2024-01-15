package application.service.impl;

import application.model.User;
import application.service.TelegramService;
import application.telegram.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final TelegramBot telegramBot;

    @Override
    public void sendNotificationToGroup(String messageText, List<User> users) {
        for (User user : users) {
            sendNotification(messageText, user);
        }
    }

    @Override
    public void sendNotification(String messageText, User user) {
        telegramBot.prepareAndSendMessage(user.getTelegramChatId(), messageText);
    }
}
