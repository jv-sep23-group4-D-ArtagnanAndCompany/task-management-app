package application.service;

import application.model.User;
import java.util.List;

public interface TelegramService {
    void sendNotificationsAboutDeadlines();

    void sendNotification(String messageText, User user);

    void sendNotification(String messageText, User user, Long id);

    void sendNotificationToGroup(String messageText, List<User> users, Long id);
}
