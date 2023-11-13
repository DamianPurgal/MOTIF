package com.deemor.motif.alert.entity;

import com.deemor.motif.user.entity.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ALERTS")
public class Alert {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "priority")
    private Long priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    private AlertStyle style;

    @Column(name = "seen")
    private boolean seen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public static Alert getBasicAlertTemplate() {
        return Alert.builder()
                .title("Title")
                .description("Description")
                .date(LocalDateTime.now())
                .priority(AlertPriority.BASIC.getPriority())
                .type(AlertType.BASIC)
                .style(AlertStyle.BASIC)
                .seen(false)
                .build();
    }

}
