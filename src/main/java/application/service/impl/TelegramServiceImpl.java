package application.service.impl;

import application.repository.TelegramChatRepository;
import application.service.TelegramService;
import application.service.impl.telegram.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final TelegramBot telegramBot;
    private final TelegramChatRepository telegramChatRepository;

    @Override
    public void sendNotificationToGroup(String messageText, List<Long> usersIds) {
        for (Long id : usersIds) {
            sendNotification(messageText, id);
        }
    }

    @Override
    public void sendNotification(String messageText, Long userId) {
        telegramChatRepository.findByUserId(userId).ifPresent(telegramChat ->
                telegramBot.prepareAndSendMessage(telegramChat.getChatId(), messageText));
    }
}
