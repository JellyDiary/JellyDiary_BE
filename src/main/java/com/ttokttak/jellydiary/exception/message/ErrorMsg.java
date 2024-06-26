package com.ttokttak.jellydiary.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorMsg {
    /* 400 BAD_REQUEST : 잘못된 요청 */ //You can only upload up to 5 images.
    REFRESH_TOKEN_NULL(BAD_REQUEST, "Refresh Token이 없습니다."),
    REFRESH_TOKEN_EXPIRED(BAD_REQUEST, "Refresh Token이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "Refresh Token이 유효하지 않습니다."),
    IMAGE_INVALID(BAD_REQUEST,"이미지가 잘못 되었습니다."),
    YOU_CAN_ONLY_UPLOAD_UP_TO_5_IMAGES(BAD_REQUEST,"게시물 이미지는 5개까지만 업로드 할 수 있습니다."),
    POST_DATE_IS_FROM_THE_PAST_TO_TODAY(BAD_REQUEST, "작성일자는 과거부터 오늘까지만 선택 가능합니다."),
    DIARY_CREATOR_CANNOT_BE_DELETED(BAD_REQUEST, "다이어리 생성자는 삭제 대상이 아닙니다."),
    CANNOT_FOLLOW_SELF(BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    CANNOT_REPLY_COMMENT_TO_REPLIES(BAD_REQUEST, "답글은 댓글에만 달 수 있으며, 답글에는 답글을 작성할 수 없습니다."),
    THE_COMMNET_YOU_REQUESTED_IS_A_REPLY(BAD_REQUEST, "요청하신 댓글은 답글입니다. 답글에는 답글 리스트가 없으므로 다시 요청해주세요."),
    CANNOT_CHAT_WITH_SELF(BAD_REQUEST, "자기 자신과의 채팅은 불가능합니다."),
    IMG_IS_NULL(BAD_REQUEST, "이미지가 null입니다."),
    INVALID_USER(BAD_REQUEST, "유효하지 않는 사용자입니다."),
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "인증된 사용자가 아닙니다."),
    NOT_LOGGED_ID(UNAUTHORIZED, "로그인이 되어있지 않습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */
    USER_ACCOUNT_DISABLED(FORBIDDEN, "사용자의 계정이 비활성화 상태입니다."),
    YOU_ARE_NOT_A_DIARY_CREATOR(FORBIDDEN, " 다이어리 생성자가 아니므로 다이어리 프로필 업데이트, 참여자 추가, 삭제 권한이 없습니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_WRITE_AND_UPDATE(FORBIDDEN, " 다이어리 생성자 이거나 쓰기 권한이 있는 사용자만이 게시물 생성 및 수정이 가능합니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE(FORBIDDEN, " 다이어리 생성자만이 게시물 삭제가 가능합니다."),
    NO_PERMISSION_TO_APPROVE_INVITATION(FORBIDDEN, "해당 초대 요청을 승인할 권한이 없습니다."),
    SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE(FORBIDDEN, "아쉽게도 구독자는 해당 다이어리의 비공개 게시글에 접근할 수 없습니다."),
    SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_AND_COMMENT(FORBIDDEN, "아쉽게도 구독자는 해당 다이어리의 비공개 게시글에 접근할 수 없으며 댓글에 태그될 수도 없습니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE(FORBIDDEN, "비공개 게시물에 접근할 권한이 없습니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_POST_AND_COMMENT(FORBIDDEN, "해당 사용자는 비공개 게시물 및 댓글에 접근할 권한이 없기에 태그할 수 없습니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE_COMMENT(FORBIDDEN, "댓글/답글은 생성자만이 삭제가 가능합니다."),
    COMMENT_AND_POST_DO_DOT_MATCH(FORBIDDEN, "요청하신 게시물과 요청하신 댓글의 게시물이 일치하지 않습니다."),
    SEARCH_WORD_MUST_NOT_BE_BLANK(FORBIDDEN, "검색어는 공백이 포함될 수 없습니다."),
    YOU_CANNOT_TAG_YOURSELF(FORBIDDEN, "자기 자신은 태그할 수 없습니다."),
    YOU_ARE_NOT_A_CHAT_ROOM_MEMBER(FORBIDDEN, "채팅방 멤버가 아니므로 채팅에 참여할 수 없습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
    USER_TAG_NOT_FOUND(NOT_FOUND, "태그된 사용자 중 존재하지 않는 사용자가 있습니다."),
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    DIARY_NOT_FOUND(NOT_FOUND, "다이어리가 존재하지 않습니다."),
    DIARY_USER_NOT_FOUND(NOT_FOUND, "다이어리에 유저 정보가 존재하지 않습니다."),
    POST_NOT_FOUND(NOT_FOUND, "게시물이 존재하지 않습니다."),
    IMG_NOT_FOUND(NOT_FOUND, "이미지가 존재하지 않습니다."),
    DIARY_ALREADY_DELETED(NOT_FOUND, "삭제된 다이어리입니다."),
    POST_ALREADY_DELETED(NOT_FOUND, "삭제된 게시물입니다."),
    POST_IMG_ALREADY_DELETED(NOT_FOUND, "삭제된 게시물 이미지입니다."),
    POST_LIKE_NOT_FOUND(NOT_FOUND, "이 게시물은 아직 좋아요를 하지 않았습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글이 존재하지 않습니다."),
    COMMENT_ALREADY_DELETED(NOT_FOUND, "삭제된 댓글/답글입니다."),
    NOTIFICATION_SETTINGS_NOT_FOUND(NOT_FOUND, "알림 설정 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(CONFLICT,"이미 가입된 사용자입니다."),
    DUPLICATE_USER_NAME(CONFLICT,"이미 사용 중인 이름입니다."),
    DUPLICATE_EMAIL(CONFLICT,"중복된 이메일입니다."),
    DUPLICATE_DIARY_USER(CONFLICT,"이미 해당 다이어리에 참여 중인 사용자입니다."),
    ALREADY_SUBSCRIBED_DIARY(CONFLICT,"이미 구독중인 다이어리입니다."),
    ALREADY_SENT_INVITATION(CONFLICT,"이미 초대 요청을 보낸 사용자입니다."),
    ALREADY_APPROVED_INVITATION_REQUEST(CONFLICT,"이미 초대 요청을 승인한 사용자입니다."),
    ALREADY_FOLLOWED_USER(CONFLICT,"이미 팔로우한 회원입니다."),

    /* 500 INTERNAL SERVER ERROR : 그 외 서버 에러 (컴파일 관련) */
    S3_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "S3 업로드 중 문제가 발생했습니다."),
    S3_DELETE_FAILED(INTERNAL_SERVER_ERROR, "S3 객체 삭제 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
