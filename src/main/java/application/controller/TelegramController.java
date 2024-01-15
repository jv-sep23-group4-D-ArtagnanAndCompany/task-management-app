package application.controller;

import application.repository.UserRepository;
import application.service.TelegramService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telegram")
public class TelegramController {
    private final UserRepository userRepository;
    private final TelegramService telegramService;

    @PostMapping("/send/{id}")
    @Operation(summary = "Send message to user by userId",
            description = "Send message to user by userId")
    public void sendMessage(@RequestBody String message, @PathVariable Long id) {
        userRepository.findUserById(id).ifPresent(user ->
                telegramService.sendNotification(message, user));
    }
}
