package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostConmmentRes {
    int postCommentIdx;
    String reply;
    int depth;
    int userIdx;
    int commentLikeCount;
    String updatedAt;
    String userId;
    String profileImg;
    int commentIdxA;
}
