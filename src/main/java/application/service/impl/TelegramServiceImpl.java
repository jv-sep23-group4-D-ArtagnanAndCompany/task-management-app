package application.service.impl;

import application.model.Task;
import application.model.User;
import application.repository.TaskRepository;
import application.service.TelegramService;
import application.telegram.TelegramBot;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private static final String DEADLINE_MESSAGE = "Deadline for the task %s with id %s is %s!";
    private static final String cron_value = "0 0 15 * * *";
    private final TelegramBot telegramBot;
    private final TaskRepository taskRepository;

    @Override
    @Scheduled(cron = cron_value)
    public void sendNotificationsAboutDeadlines() {
        taskRepository.getAllByDueDateBetweenAndStatusIsNot(LocalDate.now(),
                        LocalDate.now().plusDays(2), Task.Status.COMPLETED)
                .stream().forEach(task -> {
                    User user = task.getAssignee();
                    sendNotification(String.format(DEADLINE_MESSAGE,
                            task.getName(), task.getDueDate()), user);
                });
    }

    @Override
    public void sendNotificationToGroup(String messageText, List<User> users, Long id) {
        for (User user : users) {
            sendNotification(messageText, user, id);
        }
    }

    @Override
    public void sendNotification(String messageText, User user, Long id) {
        telegramBot.prepareAndSendMessage(user.getTelegramChatId(), messageText, id);
    }

    @Override
    public void sendNotification(String messageText, User user) {
        telegramBot.prepareAndSendMessage(user.getTelegramChatId(), messageText);
    }
}
