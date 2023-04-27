package com.example.demo.src.story.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLikeStoryReq {
    private String status;
    private int storyIdx;
}
