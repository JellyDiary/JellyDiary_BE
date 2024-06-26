package com.ttokttak.jellydiary.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessMsg {
    /* 200 OK : 성공 */
    LOGIN_SUCCESS(OK, "로그인 완료"),
    LOGOUT_SUCCESS(OK, "로그아웃 완료"),
    DELETE_USER_SUCCESS(OK, "회원 탈퇴 완료"),
    TOKEN_REISSUED_SUCCESS(OK, "토큰 재발급 완료"),
    SEARCH_DIARY_SUCCESS(OK, "다이어리 정보 조회 완료"),
    SEARCH_DIARY_USER_LIST_SUCCESS(OK, "다이어리 참여자, 생성자 유저 조회 완료"),
    SEARCH_DIARY_USER_ROLE(OK, "다이어리 유저(권한) 조회 완료"),
    SEARCH_MY_DIARY_LIST_SUCCESS(OK, "내가 구독 또는 참여 중인 다이어리 리스트 조회 완료"),
    SEARCH_TARGET_USER_FEED_INFO_SUCCESS(OK, "타켓 유저 피드 정보 조회 완료"),
    FOLLOW_REQUEST_SUCCESS(OK, "팔로우 신청 완료"),
    SEARCH_TARGET_USER_FOLLOWER_LIST_SUCCESS(OK, "타켓 유저의 팔로워 리스트 조회 완료"),
    SEARCH_TARGET_USER_FOLLOW_LIST_SUCCESS(OK, "타켓 유저의 팔로우 리스트 조회 완료"),
    UNFOLLOW_SUCCESS(OK, "팔로우 취소 완료"),
    SEARCH_TARGET_USER_FEED_LIST_SUCCESS(OK, "타켓 유저 피드 리스트 조회 완료"),
    SEARCH_CHAT_ROOM_ID_SUCCESS(OK, "채팅방 아이디 조회 완료"),
    SEARCH_MY_CHAT_LIST_SUCCESS(OK, "나의 채팅 리스트 조회 완료"),
    SEARCH_CHAT_MESSAGES_SUCCEEDED(OK, "채팅 내역 상세 조회 완료"),

    GET_USER_PROFILE_SUCCESS(OK, "유저 프로필 조회 완료"),
    UPDATE_USER_PROFILE_IMAGE_SUCCESS(OK, "유저 프로필 이미지 수정 완료"),
    UPDATE_USER_PROFILE_SUCCESS(OK, "유저 프로필 수정 완료"),
    UPDATE_USER_NOTIFICATION_SETTING_SUCCESS(OK, "알림 수신 설정 완료"),
    USER_NAME_CHECK_SUCCESS(OK,"사용 가능한 이름입니다."),


    UPDATE_DIARY_PROFILE_SUCCESS(OK, "다이어리 프로필 수정 완료"),
    UPDATE_DIARY_PROFILE_IMAGE_SUCCESS(OK, "다이어리 프로필 이미지 수정 완료"),
    UPDATE_DIARY_USER_ROLE_SUCCESS(OK, "다이어리 유저 role 수정 완료"),
    UPDATE_DIARY_USER_IS_INVITED_SUCCESS(OK, "다이어리 유저 isInvited 수정(초대 승인) 완료"),

    DELETE_DIARY_USER_SUCCESS(OK, "다이어리 유저 삭제 완료"),
    DELETE_DIARY_PROFILE_SUCCESS(OK, "다이어리 프로필 삭제 완료"),


    UPDATE_POST_SUCCESS(OK, "게시물 수정 완료"),
    DELETE_POST_SUCCESS(OK, "게시물 삭제 완료"),
    GET_LIST_POST_SUCCESS(OK, "게시물 리스트 조회 완료"),
    GET_ONE_POST_SUCCESS(OK, "게시물 상세 조회 완료"),
    CREATE_LIKE_POST_SUCCESS(OK, "게시물 좋아요 등록 완료"),
    GET_LIKE_POST_SUCCESS(OK, "게시물 좋아요 상태 조회 완료"),
    DELETE_LIKE_POST_SUCCESS(OK, "게시물 좋아요 취소 완료"),
    SEARCH_USER_SUCCESS(OK, "사용자 검색 성공"),
    GET_SNS_LIST_SUCCESS(OK, "sns 게시물 리스트 조회 성공"),
    NOTIFICATION_LIST_SUCCESS(OK, "알림 리스트 조회 성공"),
    NOTIFICATION_DELETE_SUCCESS(OK, "알림 삭제 완료"),


    /* 201 CREATED : 생성 */
    CREATE_DIARY_PROFILE_SUCCESS(CREATED, "다이어리 프로필 생성 완료"),
    CREATE_DIARY_USER_SUCCESS(CREATED, "다이어리 유저 생성 완료"),
    CREATE_POST_SUCCESS(CREATED, "게시물 생성 완료"),
    CREATE_COMMENT_SUCCESS(CREATED, "게시물에 댓글 작성 완료"),
    REPLY_CREATE_COMMENT_SUCCESS(CREATED, "게시물 댓글에 답글 작성 완료"),
    DELETE_COMMENT_SUCCESS(OK, "댓글 삭제 완료"),
    GET_LIST_POST_COMMENT(OK, "게시물 댓글 리스트 조회 완료"),
    GET_LIST_POST_REPLY_COMMENT(OK, "타겟 댓글의 답글 리스트 조회 완료");


    private final HttpStatus httpStatus;
    private final String detail;
}
