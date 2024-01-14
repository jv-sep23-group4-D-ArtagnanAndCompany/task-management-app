package application.service.impl.telegram;

import application.config.TelegramBotConfig;
import application.model.TelegramChat;
import application.model.User;
import application.repository.TelegramChatRepository;
import application.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final String EMAIL_REGEX =
            "^[\\w]{1,}[\\w.+-]{0,}@[\\w-]{2,}([.][a-zA-Z]{2,}|[.][\\w-]{2,}[.][a-zA-Z]{2,})$";
    private static final String START_COMMAND = "/start";
    private static final String UNKNOWN_COMMAND = "Unknown command!";
    private static final String HELLO_MESSAGE = "Hello, %s! Please, write your email to register.";
    private static final String REGISTERED_MESSAGE =
            "Great, now you will receive notifications about your tasks";
    private static final String RE_REGISTRATION_MESSAGE =
            "It seems that you have already subscribed to receive notifications about your tasks";
    private static final String NON_EXIST_EMAIL =
            "Sorry, no user with this email address was found";

    private final TelegramBotConfig telegramBotConfig;
    private final TelegramChatRepository telegramChatRepository;
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
            Message message = update.getMessage();
            String messageText = message.getText();
            Long chatId = message.getChatId();

            if (messageText.matches(EMAIL_REGEX)) {
                registerUser(chatId, messageText);
            } else if (messageText.equals(START_COMMAND)) {
                prepareAndSendMessage(chatId,
                        String.format(HELLO_MESSAGE, message.getChat().getFirstName()));
            } else {
                prepareAndSendMessage(chatId, UNKNOWN_COMMAND);
            }
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
        if (userByEmail.isEmpty()) {
            prepareAndSendMessage(chatId, NON_EXIST_EMAIL);
        } else {
            Optional<TelegramChat> chatById = telegramChatRepository.findById(chatId);
            if (chatById.isEmpty()) {
                TelegramChat chat = new TelegramChat()
                        .setChatId(chatId)
                        .setUserId(userByEmail.get().getId());
                telegramChatRepository.save(chat);
                prepareAndSendMessage(chatId, REGISTERED_MESSAGE);
            } else {
                prepareAndSendMessage(chatId, RE_REGISTRATION_MESSAGE);
            }
        }
    }
}
