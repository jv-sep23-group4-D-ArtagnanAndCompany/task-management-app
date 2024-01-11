package application.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Data
@Accessors(chain = true)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime timeStamp;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
