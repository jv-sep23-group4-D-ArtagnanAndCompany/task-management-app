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
    private final TelegramBot telegramBot;
    private final TaskRepository taskRepository;

    @Override
    @Scheduled(cron = "0 0 15 * * *")
    public void sendNotificationsAboutDeadlines() {
        taskRepository.getAllByDueDateBetweenAndStatusIsNot(LocalDate.now(),
                        LocalDate.now().plusDays(2), Task.Status.COMPLETED)
                .stream().forEach(task -> {
                    User user = task.getAssignee();
                    sendNotification(String.format("Deadline for the task %s is %s!",
                            task.getName(), task.getDueDate()), user);
                });
    }

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
