package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostLikeListRes {
    private int userIdx;
    private String name;
    private String userId;
    private String profileImg;
    private String followStatus;
}
