package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryUserRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryUserUpdateRoleRequestDto;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.mapper.DiaryUserMapper;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryUserServiceImpl implements DiaryUserService{

    private final DiaryProfileRepository diaryProfileRepository;

    private final DiaryUserRepository diaryUserRepository;

    private final UserRepository userRepository;

    private final DiaryUserMapper diaryUserMapper;

    @Override
    @Transactional
    public ResponseDto<?> getDiaryParticipantsList(Long diaryId) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        List<DiaryUserEntity> diaryUserEntities = diaryUserRepository.findByDiaryIdAndDiaryRoleNot(diaryProfileEntity, DiaryUserRoleEnum.SUBSCRIBE);

        return ResponseDto.builder()
                .statusCode(SEARCH_DIARY_USER_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_DIARY_USER_LIST_SUCCESS.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDtoList(diaryUserEntities))
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> createDiaryUser(DiaryUserRequestDto diaryUserRequestDto, CustomOAuth2User customOAuth2User) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryUserRequestDto.getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryUserEntity loginUserInDiary = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR));
        if(!loginUserInDiary.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        UserEntity invitedUserEntity = userRepository.findById(diaryUserRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Optional<DiaryUserEntity> invitedUserInDiary = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, invitedUserEntity);
        if(invitedUserInDiary.isPresent()){
            throw new CustomException(DUPLICATE_DIARY_USER);
        }

        DiaryUserEntity diaryUserEntity = DiaryUserEntity.builder()
                .diaryId(diaryProfileEntity)
                .userId(invitedUserEntity)
                .isInvited(false)
                .diaryRole(DiaryUserRoleEnum.READ)
                .build();
        diaryUserRepository.save(diaryUserEntity);

        return ResponseDto.builder()
                .statusCode(CREATE_DIARY_USER_SUCCESS.getHttpStatus().value())
                .message(CREATE_DIARY_USER_SUCCESS.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDto(diaryUserEntity))
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> updateDiaryParticipantsRolesList(Long diaryId, List<DiaryUserUpdateRoleRequestDto> updateRequestDtoList, CustomOAuth2User customOAuth2User) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryUserEntity loginUserInDiary = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR));
        if(!loginUserInDiary.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        for(DiaryUserUpdateRoleRequestDto dto : updateRequestDtoList){
            DiaryUserEntity diaryUserEntity = diaryUserRepository.findById(dto.getDiaryUserId())
                    .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

            diaryUserEntity.DiaryUserRoleUpdate(DiaryUserRoleEnum.valueOf(dto.getDiaryRole()));
        }

        List<DiaryUserEntity> diaryUserEntities = diaryUserRepository.findByDiaryIdAndDiaryRoleNot(diaryProfileEntity, DiaryUserRoleEnum.SUBSCRIBE);

        return ResponseDto.builder()
                .statusCode(UPDATE_DIARY_USER_ROLE_SUCCESS.getHttpStatus().value())
                .message(UPDATE_DIARY_USER_ROLE_SUCCESS.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDtoList(diaryUserEntities))
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> updateDiaryUserIsInvited(Long diaryUserId) {
        DiaryUserEntity diaryUserEntity = diaryUserRepository.findById(diaryUserId)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        diaryUserEntity.isInvitedUpdate(true);

        return ResponseDto.builder()
                .statusCode(UPDATE_DIARY_USER_IS_INVITED_SUCCESS.getHttpStatus().value())
                .message(UPDATE_DIARY_USER_IS_INVITED_SUCCESS.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDto(diaryUserEntity))
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> deleteDiaryUser(Long diaryUserId, CustomOAuth2User customOAuth2User) {
        DiaryUserEntity diaryUserEntity = diaryUserRepository.findById(diaryUserId)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryUserEntity.getDiaryId().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryUserEntity loginUserInDiary = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));


        if(!loginUserInDiary.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)
            && !Objects.equals(loginUserEntity.getUserId(), diaryUserEntity.getUserId().getUserId())){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        if(loginUserInDiary.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)
                && Objects.equals(loginUserEntity.getUserId(), diaryUserEntity.getUserId().getUserId())){
            throw new CustomException(DIARY_CREATOR_CANNOT_BE_DELETED);
        }

        diaryUserRepository.delete(diaryUserEntity);

        return ResponseDto.builder()
                .statusCode(DELETE_DIARY_USER_SUCCESS.getHttpStatus().value())
                .message(DELETE_DIARY_USER_SUCCESS.getDetail())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> getUserRoleInDiary(Long diaryId, CustomOAuth2User customOAuth2User) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryUserEntity loginUserInDiary = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));


        return ResponseDto.builder()
                .statusCode(SEARCH_DIARY_USER_ROLE.getHttpStatus().value())
                .message(SEARCH_DIARY_USER_ROLE.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDto(loginUserInDiary))
                .build();
    }

}
