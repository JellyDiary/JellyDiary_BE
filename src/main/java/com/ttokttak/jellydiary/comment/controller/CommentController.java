package com.ttokttak.jellydiary.comment.controller;

import com.ttokttak.jellydiary.comment.dto.CommentCreateRequestDto;
import com.ttokttak.jellydiary.comment.service.CommentService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "[댓글 작성] api")
    @PostMapping("/{postId}")
    public ResponseEntity<ResponseDto<?>> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDto commentCreateRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.createComment(postId, commentCreateRequestDto, customOAuth2User));
    }

    @Operation(summary = "댓글에 답글 작성", description = "[댓글에 답글 작성] api")
    @PostMapping("/{postId}/{commentId}")
    public ResponseEntity<ResponseDto<?>> createReplyComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentCreateRequestDto commentCreateRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.createReplyComment(postId, commentId, commentCreateRequestDto, customOAuth2User));
    }

    @Operation(summary = "댓글/답글 삭제", description = "[댓글/답글 삭제] api")
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<ResponseDto<?>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.deleteComment(postId, commentId, customOAuth2User));
    }

    @Operation(summary = "해당 게시물의 댓글 리스트 조회", description = "[해당 게시물의 댓글 리스트 조회] api")
    @GetMapping("/commentList/{postId}")
    public ResponseEntity<ResponseDto<?>> getCommentList(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.getCommentList(postId, customOAuth2User));
    }

    @Operation(summary = "타겟 댓글의 답글 리스트 조회", description = "[타겟 댓글의 답글 리스트 조회] api")
    @GetMapping("/{postId}/{commentId}")
    public ResponseEntity<ResponseDto<?>> getReplyCommentList(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.getReplyCommentList(postId, commentId, customOAuth2User));
    }


}
