package com.ttokttak.jellydiary.comment.service;

import com.ttokttak.jellydiary.comment.dto.CommentCreateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface CommentService {
    ResponseDto<?> createComment(Long postId, CommentCreateRequestDto commentCreateRequestDto, CustomOAuth2User customOAuth2User);

    ResponseDto<?> createReplyComment(Long postId, Long commentId, CommentCreateRequestDto commentCreateRequestDto, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getCommentList(Long postId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getReplyCommentList(Long postId, Long commentId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> deleteComment(Long postId, Long commentId, CustomOAuth2User customOAuth2User);
}
