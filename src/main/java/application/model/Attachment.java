package application.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE attachments SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Data
@Accessors(chain = true)
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Task task;
    @Column(nullable = false)
    private String dropBoxFileId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadDate;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
