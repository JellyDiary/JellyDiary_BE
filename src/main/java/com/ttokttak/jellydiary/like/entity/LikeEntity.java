package com.ttokttak.jellydiary.like.entity;

import com.ttokttak.jellydiary.diary.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_like")
public class LikeEntity {
    @EmbeddedId
    private PostLikeCompositeKey id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @MapsId("diaryPostId")
    @ManyToOne(fetch = FetchType.LAZY)
    private DiaryPostEntity diaryPost;
}