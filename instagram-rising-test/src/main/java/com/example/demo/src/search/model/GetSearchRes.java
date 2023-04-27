package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchRes {
    private int userIdx;
    private String name;
    private String detail;
    private String img;
    private int cnt;
}
