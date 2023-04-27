package com.example.demo.src.story.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostStoryReq {
    private String content;
    private String img;
    private List<Integer> tagUserIdx;
    private String tagLocationIdx;
}
