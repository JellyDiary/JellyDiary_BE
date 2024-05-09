package com.ttokttak.jellydiary.user.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.UserNameCheckRequestDto;
import com.ttokttak.jellydiary.user.dto.UserProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.service.UserService;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 프로필 조회", description = "[유저 프로필 조회] api")
    @GetMapping("profile")
    public ResponseEntity<ResponseDto<?>> getUserProflie(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(userService.getUserProflie(customOAuth2User));
    }

    @Operation(summary = "유저 프로필 이미지 수정", description = "[유저 프로필 이미지 수정] api")
    @PatchMapping("profile/image")
    public ResponseEntity<ResponseDto<?>> updateUserProfileImg(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestParam("newProfileImg") MultipartFile newProfileImg) {
        return ResponseEntity.ok(userService.updateUserProfileImg(customOAuth2User, newProfileImg));
    }

    @Operation(summary = "유저 이름 중복 검증", description = "[유저 이름 중복 검증] api")
    @PostMapping("/profile/ckeckUserName")
    public ResponseEntity<ResponseDto<?>> checkUserName(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody @Valid UserNameCheckRequestDto userNameCheckRequestDto) {
        return ResponseEntity.ok(userService.checkUserName(customOAuth2User, userNameCheckRequestDto));
    }
}
