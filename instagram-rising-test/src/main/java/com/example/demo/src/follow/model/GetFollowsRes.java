package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowsRes {
    private int userIdx;
    private String name;
    private String profileImg;
    private String id;
    private String status;
    private String followYn;
}
