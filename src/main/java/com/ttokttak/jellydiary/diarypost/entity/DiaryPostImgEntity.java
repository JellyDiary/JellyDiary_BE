package com.ttokttak.jellydiary.diarypost.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE diary_post_img SET is_deleted = true WHERE post_img_id = ?")
@Table(name = "diary_post_img")
public class DiaryPostImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgId;

    @Column(nullable = false)
    private String imageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DiaryPostEntity diaryPost;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public DiaryPostImgEntity(Long postImgId, String imageLink, DiaryPostEntity diaryPost, Boolean isDeleted) {
        this.postImgId = postImgId;
        this.imageLink = imageLink;
        this.diaryPost = diaryPost;
        this.isDeleted = isDeleted;
    }
}
