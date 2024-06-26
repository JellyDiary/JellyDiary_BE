package com.ttokttak.jellydiary.chat.service;

import com.ttokttak.jellydiary.chat.dto.ChatMessageListResponseDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.chat.mapper.ChatMessageMapper;
import com.ttokttak.jellydiary.chat.repository.ChatMessageRepository;
import com.ttokttak.jellydiary.chat.repository.ChatRoomRepository;
import com.ttokttak.jellydiary.chat.repository.ChatUserRepository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.notification.service.NotificationServiceImpl;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageMapper chatMessageMapper;

    private final NotificationSettingRepository notificationSettingRepository;

    private final NotificationServiceImpl notificationServiceImpl;

    private final DiaryProfileRepository diaryProfileRepository;

    @Override
    @Transactional
    public ChatMessageResponseDto createIncompleteChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto ) {
        UserEntity loginUserEntity = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        ChatUserEntity chatUserEntity = chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .chatMessage(chatMessageRequestDto.getChatMessage())
                .chatRoomId(chatRoomEntity)
                .userId(loginUserEntity)
                .build();
        ChatMessageEntity savedChatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        List<UserEntity> userListByChatRoomId = chatUserRepository.findUsersByChatRoomId(chatRoomId);
        for(UserEntity user : userListByChatRoomId){
            if(user.equals(loginUserEntity))
                continue;

            Optional<NotificationSettingEntity> notificationSettingEntity = notificationSettingRepository.findByUser(user);
            if (notificationSettingEntity.isPresent()) {
                if (user.getNotificationSetting() && notificationSettingEntity.get().getDm()) {
                    notificationServiceImpl.send(loginUserEntity.getUserId(), user.getUserId(), NotificationType.DM_MESSAGE_REQUEST, NotificationType.DM_MESSAGE_REQUEST.makeContent(loginUserEntity.getUserName()), chatRoomId);
                }
            }
        }



        return chatMessageMapper.entityToChatMessageResponseDto(savedChatMessageEntity);
    }

    @Override
    @Transactional
    public ChatMessageResponseDto createChatMessage(Long chatRoomId, String message, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        ChatUserEntity chatUserEntity = chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .chatMessage(message)
                .chatRoomId(chatRoomEntity)
                .userId(loginUserEntity)
                .build();
        ChatMessageEntity savedChatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        return chatMessageMapper.entityToChatMessageResponseDto(savedChatMessageEntity);
    }

    @Override
    public ResponseDto<?> getMessagesByChatRoomId(Pageable pageable, Long chatRoomId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        Slice<ChatMessageEntity> chatMessageEntityPage = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomEntity, pageable);

        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageEntityPage.getContent().stream()
                .map(chatMessageMapper::entityToChatMessageResponseDto)
                .collect(Collectors.toList());

        ChatMessageListResponseDto chatMessageListResponseDto = ChatMessageListResponseDto.builder()
                .chatMessageList(chatMessageResponseDtoList)
                .hasPrevious(chatMessageEntityPage.hasPrevious())
                .hasNext(chatMessageEntityPage.hasNext())
                .page(chatMessageEntityPage.getNumber())
                .build();

        String[] splitRoomName = chatRoomEntity.getChatRoomName().split("_");
        if(splitRoomName[0].equals("group")){
            DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(Long.parseLong(splitRoomName[1]))
                    .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

            chatMessageListResponseDto.isDiaryDeleted(diaryProfileEntity.getIsDiaryDeleted());
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_CHAT_MESSAGES_SUCCEEDED.getHttpStatus().value())
                .message(SEARCH_CHAT_MESSAGES_SUCCEEDED.getDetail())
                .data(chatMessageListResponseDto)
                .build();
    }
}
