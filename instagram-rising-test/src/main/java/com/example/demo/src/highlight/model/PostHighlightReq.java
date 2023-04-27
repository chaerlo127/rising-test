package com.example.demo.src.highlight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostHighlightReq {
    private List<Integer> storyIdx;
    private String name;
    private String coverImg;
}
