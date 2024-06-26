package com.ttokttak.jellydiary.like.service;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.dto.PostLikeGetResponseDto;
import com.ttokttak.jellydiary.like.dto.PostLikeMapper;
import com.ttokttak.jellydiary.like.entity.PostLikeCompositeKey;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.notification.service.NotificationServiceImpl;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final PostLikeMapper postLikeMapper;
    private final NotificationServiceImpl notificationServiceImpl;

    @Transactional
    @Override
    public ResponseDto<?> createPostLike(Long postId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        //복합키 생성
        PostLikeCompositeKey postLikeCompositeKey = postLikeMapper.dtoToCompositeKeyEntity(userEntity.getUserId(), diaryPostEntity.getPostId());
        //복합키와 user, diaryPost로 PostLikeEntity 생성
        PostLikeEntity postLikeEntity = postLikeMapper.compositeKeyAndUserAndPostToPostLikeEntity(postLikeCompositeKey, userEntity, diaryPostEntity);

        postLikeRepository.save(postLikeEntity);

        //게시물 생성자에게 알림 발송
        Optional<NotificationSettingEntity> notificationSettingEntity = notificationSettingRepository.findByUser(diaryPostEntity.getUser());
        if (notificationSettingEntity.isPresent()) {
            if (diaryPostEntity.getUser().getNotificationSetting() && notificationSettingEntity.get().getPostLike()) {
                Long receiverId = diaryPostEntity.getUser().getUserId();
                notificationServiceImpl.send(userEntity.getUserId(), receiverId, NotificationType.POST_LIKE_REQUEST, NotificationType.POST_LIKE_REQUEST.makeContent(userEntity.getUserName()), diaryPostEntity.getPostId());
            }
        }

        return ResponseDto.builder()
                .statusCode(CREATE_LIKE_POST_SUCCESS.getHttpStatus().value())
                .message(CREATE_LIKE_POST_SUCCESS.getDetail())
                .build();
    }

    @Override
    public ResponseDto<?> getPostLike(Long postId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        Optional<PostLikeEntity> postLikeEntity = postLikeRepository.findByUserAndDiaryPost(userEntity, diaryPostEntity);
        boolean likeState = false;
        if (postLikeEntity.isPresent()) {
            likeState = true;
        }

        PostLikeGetResponseDto postLikeGetResponseDto = new PostLikeGetResponseDto(likeState);

        return ResponseDto.builder()
                .statusCode(GET_LIKE_POST_SUCCESS.getHttpStatus().value())
                .message(GET_LIKE_POST_SUCCESS.getDetail())
                .data(postLikeGetResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> deletePostLike(Long postId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        Optional<PostLikeEntity> postLikeEntity = postLikeRepository.findByUserAndDiaryPost(userEntity, diaryPostEntity);
        if (postLikeEntity.isPresent()) {
            postLikeRepository.delete(postLikeEntity.get());
        } else {
            throw new CustomException(POST_LIKE_NOT_FOUND);
        }

        return ResponseDto.builder()
                .statusCode(DELETE_LIKE_POST_SUCCESS.getHttpStatus().value())
                .data(DELETE_LIKE_POST_SUCCESS.getDetail())
                .build();
    }
}
