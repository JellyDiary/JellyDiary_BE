package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class CommentGetCommentInfoDto {
    private Long userId;
    private String userName;
    private String userProfileImg;
    private Long commentId;
    private String createdAt;
    private Set<CommentUserTagInfoDto> userTag;
    private String commentContent;
    private Long replyCount;

    @Builder
    public CommentGetCommentInfoDto(Long userId, String userName, String userProfileImg, Long commentId, String createdAt, Set<CommentUserTagInfoDto> userTag, String commentContent, Long replyCount) {
        this.userId = userId;
        this.userName = userName;
        this.userProfileImg = userProfileImg;
        this.commentId = commentId;
        this.createdAt = createdAt;
        this.userTag = userTag;
        this.commentContent = commentContent;
        this.replyCount = replyCount;
    }
}
