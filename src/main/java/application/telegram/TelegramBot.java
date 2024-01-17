package application.telegram;

import application.config.TelegramBotConfig;
import application.exception.EntityNotFoundException;
import application.model.Comment;
import application.model.Task;
import application.model.User;
import application.repository.CommentRepository;
import application.repository.TaskRepository;
import application.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final String USER_FIND_EXCEPTION = "Can't find user by chat id ";
    private static final String COMMENT_FIND_EXCEPTION = "Can't find a comment by id ";
    private static final String TASK_FIND_EXCEPTION = "Can't find a task by id ";
    private static final String UNSUBSCRIBED_EXCEPTION = "You are not subscribed to notifications";
    private static final String WITH_ID = " with id ";
    private static final String BUTTON_TASK_NAME = "See task name";
    private static final String BUTTON_TASK_DESCRIPTION = "See task description";
    private static final String BUTTON_COMMENT_TEXT = "See comment text";
    private static final String BUTTON_TASK_PRIORITY = "See task priority";
    private static final Integer COMMENT_TEXT_INDEX_ID = 25;
    private static final Integer TASK_NAME_INDEX_ID = 22;
    private static final Integer TASK_DESCRIPTION_INDEX_ID = 29;
    private static final Integer TASK_PRIORITY_INDEX_ID = 26;
    private static final String PREFIX_COMMENT = "A new comment";
    private static final String PREFIX_UPDATE_TASK = "A task";
    private static final String PREFIX_CREATE_TASK = "A new task";
    private static final String EMAIL_REGEX =
            "^[\\w]{1,}[\\w.+-]{0,}@[\\w-]{2,}([.][a-zA-Z]{2,}|[.][\\w-]{2,}[.][a-zA-Z]{2,})$";
    private static final String START_COMMAND = "/start";
    private static final String STOP_COMMAND = "/stop";
    private static final String UNKNOWN_COMMAND = "Unknown command!";
    private static final String UNSUBSCRIBED_MESSAGE = "You have unsubscribed "
            + "from notifications";
    private static final String HELLO_MESSAGE = "Hello, %s! Please, write your email to register.";
    private static final String REGISTERED_MESSAGE =
            "Great, now you will receive notifications about your tasks. "
                    + "You can stop that by /stop";
    private static final String RE_REGISTRATION_MESSAGE =
            "It seems that you have already subscribed to receive notifications about your tasks";
    private static final String NON_EXIST_EMAIL =
            "Sorry, no user with this email address was found";
    private static final Long DEFAULT_EMPTY_CHAT_ID = -1L;
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    private final TelegramBotConfig telegramBotConfig;
    private final UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            sendFunctionalMessageToUser(update);
        } else if (update.hasCallbackQuery()) {
            sendMessageToUserAboutSomeDetails(update);
        }
    }

    public void prepareAndSendMessage(Long chatId, String messageText, Long id) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        addNeededButtons(message, messageText, id);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareAndSendMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerUser(Long chatId, String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            Long telegramChatId = user.getTelegramChatId();
            if (telegramChatId.equals(DEFAULT_EMPTY_CHAT_ID)) {
                userRepository.save(user.setTelegramChatId(chatId));
                prepareAndSendMessage(chatId, REGISTERED_MESSAGE);
            } else {
                prepareAndSendMessage(chatId, RE_REGISTRATION_MESSAGE);
            }
        } else {
            prepareAndSendMessage(chatId, NON_EXIST_EMAIL);
        }
    }

    private void addNeededButtons(SendMessage message, String messageText, Long id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (messageText.startsWith(PREFIX_COMMENT)) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button
                    = new InlineKeyboardButton(BUTTON_COMMENT_TEXT + " ⚡");
            button.setCallbackData(BUTTON_COMMENT_TEXT + WITH_ID + id);
            row.add(button);
            rows.add(row);
        } else if (messageText.startsWith(PREFIX_CREATE_TASK)
                || messageText.startsWith(PREFIX_UPDATE_TASK)) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton firstButton
                    = new InlineKeyboardButton(BUTTON_TASK_NAME + " 🗒");
            firstButton.setCallbackData(BUTTON_TASK_NAME + WITH_ID + id);
            row.add(firstButton);
            rows.add(row);
            row = new ArrayList<>();
            InlineKeyboardButton secondButton
                    = new InlineKeyboardButton(BUTTON_TASK_DESCRIPTION + " 📝");
            secondButton.setCallbackData(BUTTON_TASK_DESCRIPTION + WITH_ID + id);
            row.add(secondButton);
            rows.add(row);
            row = new ArrayList<>();
            InlineKeyboardButton thirdButton
                    = new InlineKeyboardButton(BUTTON_TASK_PRIORITY + " 📄");
            thirdButton.setCallbackData(BUTTON_TASK_PRIORITY + WITH_ID + id);
            row.add(thirdButton);
            rows.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    private void sendFunctionalMessageToUser(Update update) {
        Message message = update.getMessage();
        String messageText = update.getMessage().getText();
        Long chatId = message.getChatId();
        if (messageText.matches(EMAIL_REGEX)) {
            registerUser(chatId, messageText);
        } else if (messageText.equals(STOP_COMMAND)) {
            User user = findUserByChatId(chatId);
            userRepository.save(user.setTelegramChatId(null));
            prepareAndSendMessage(chatId, UNSUBSCRIBED_MESSAGE);
        } else if (messageText.equals(START_COMMAND)) {
            prepareAndSendMessage(chatId,
                    String.format(HELLO_MESSAGE, message.getChat().getFirstName()));
        } else {
            prepareAndSendMessage(chatId, UNKNOWN_COMMAND);
        }
    }

    private void sendMessageToUserAboutSomeDetails(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String buttonTitle = update.getCallbackQuery().getData();
        if (buttonTitle.startsWith(BUTTON_COMMENT_TEXT)) {
            Long commentId = Long.valueOf(buttonTitle.substring(COMMENT_TEXT_INDEX_ID));
            Comment comment = findCommentById(commentId);
            prepareAndSendMessage(chatId, comment.getText());
        } else if (buttonTitle.startsWith(BUTTON_TASK_NAME)) {
            Long taskId = Long.valueOf(buttonTitle.substring(TASK_NAME_INDEX_ID));
            Task task = findTaskById(taskId);
            prepareAndSendMessage(chatId, task.getName());
        } else if (buttonTitle.startsWith(BUTTON_TASK_DESCRIPTION)) {
            Long taskId = Long.valueOf(buttonTitle.substring(TASK_DESCRIPTION_INDEX_ID));
            Task task = findTaskById(taskId);
            prepareAndSendMessage(chatId, task.getDescription());
        } else {
            Long taskId = Long.valueOf(buttonTitle.substring(TASK_PRIORITY_INDEX_ID));
            Task task = findTaskById(taskId);
            prepareAndSendMessage(chatId, task.getPriority().toString());
        }
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(TASK_FIND_EXCEPTION
                + id));
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(COMMENT_FIND_EXCEPTION
                + id));
    }

    private User findUserByChatId(Long id) {
        try {
            return userRepository.findUserByTelegramChatId(id).orElseThrow(()
                    -> new EntityNotFoundException(USER_FIND_EXCEPTION
                    + id));
        } catch (EntityNotFoundException exception) {
            prepareAndSendMessage(id, UNSUBSCRIBED_EXCEPTION);
            throw new RuntimeException(UNSUBSCRIBED_EXCEPTION);
        }
    }
}
