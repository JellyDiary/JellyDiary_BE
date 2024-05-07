package com.ttokttak.jellydiary.user.entity;

import com.ttokttak.jellydiary.user.dto.UserCreateDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Column
    private String userDesc;

    @Column
    private String profileImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStateEnum userState;

    @Column
    private Boolean notificationSetting;

    @Builder
    public UserEntity(Long userId, String oauthId, ProviderType providerType, String userEmail, String userName, String userDesc, String profileImg, Authority authority, UserStateEnum userState, Boolean notificationSetting) {
        this.userId = userId;
        this.oauthId = oauthId;
        this.providerType = providerType;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userDesc = userDesc;
        this.profileImg = profileImg;
        this.authority = authority;
        this.userState = userState;
        this.notificationSetting = notificationSetting;
    }

    public UserEntity(UserCreateDto userCreateDto) {
        this.oauthId = userCreateDto.getOauthId();
        this.providerType = userCreateDto.getProviderType();
        this.userEmail = userCreateDto.getUserEmail();
        this.userName = userCreateDto.getUserName();
        this.authority = userCreateDto.getAuthority();
        this.userState = userCreateDto.getUserState();
        this.notificationSetting = userCreateDto.getNotificationSetting();
    }
}