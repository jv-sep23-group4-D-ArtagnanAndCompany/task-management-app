package application.service;

import java.util.List;

public interface TelegramService {
    void sendNotificationToGroup(String messageText, List<Long> usersIds);

    void sendNotification(String messageText, Long userId);
}
