package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentRes {
    int commentIdx;
    String reply;
    int commentAIdx;
    int depth;
    int userIdx;
    int postIdx;
}
