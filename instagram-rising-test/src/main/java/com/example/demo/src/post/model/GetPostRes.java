package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostRes {
    int postIdx;
    String content;
    int userIdx;
    String userId;
    String profileImgUrl;
    int postLikeCount;
    String updateAt;
    List<GetPostImgRes> postImgRes;
    List<GetPostConmmentRes> postContentRes;

}
