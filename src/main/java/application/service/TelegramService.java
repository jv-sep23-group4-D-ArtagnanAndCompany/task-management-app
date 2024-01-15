package application.service;

import application.model.User;
import java.util.List;

public interface TelegramService {
    void sendNotificationToGroup(String messageText, List<User> users);

    void sendNotification(String messageText, User user);
}
