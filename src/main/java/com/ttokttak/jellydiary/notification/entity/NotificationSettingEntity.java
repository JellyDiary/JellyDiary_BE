package com.ttokttak.jellydiary.notification.entity;

import com.ttokttak.jellydiary.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification_setting")
public class NotificationSettingEntity {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Boolean postLike;

    @Column(nullable = false)
    private Boolean postComment;

    @Column(nullable = false)
    private Boolean postCreated;

    @Column(nullable = false)
    private Boolean commentTag;

    @Column(nullable = false)
    private Boolean newFollower;

    @Column(nullable = false)
    private Boolean dm;
}
