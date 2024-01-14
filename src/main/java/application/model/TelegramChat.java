package application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE telegram_chats SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Data
@Accessors(chain = true)
@Table(name = "telegram_chats")
public class TelegramChat {
    @Id
    @Column(unique = true)
    private Long chatId;
    @Column(unique = true, nullable = false)
    private Long userId;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
