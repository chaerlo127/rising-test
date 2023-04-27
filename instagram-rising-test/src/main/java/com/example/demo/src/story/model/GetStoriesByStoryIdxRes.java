package com.example.demo.src.story.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoriesByStoryIdxRes {
    private String storyImg;
    private String profileImg;
    private String userId;
    private String postHourAgo;
    private String content;
    private String tagLocation;
    private List<GetTagUsersRes> tagUsers;
}
