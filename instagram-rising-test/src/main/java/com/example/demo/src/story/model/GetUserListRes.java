package com.example.demo.src.story.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserListRes {
    private int userIdx;
    private String userId;
    private String name;
    private String img;
    private String likeYn;
    private int cnt;
}
