package com.example.demo.src.story.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoriesRes {
    private int storyIdx;
    private List<GetStoriesByStoryIdxRes> getStoriesByStoryIdxRes;
}
