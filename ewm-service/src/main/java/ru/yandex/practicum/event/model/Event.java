package ru.yandex.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.enums.EventState;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long id;
    @Column
    private String title;
    @Column
    private String annotation;
    @Column
    private String description;
    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column
    private Boolean paid;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Long participantLimit;
    @Column(name = "request_moderation", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
}
