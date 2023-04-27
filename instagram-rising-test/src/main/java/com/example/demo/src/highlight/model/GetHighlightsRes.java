package com.example.demo.src.highlight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetHighlightsRes {
    private int userIdx;
    private int highlightIdx;
    private String coverImg;
    private String groupName;

}
