package com.example.demo.src.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileUserRes {
        private int userIdx;
        private String userId;
        private String name;
        private String introduction;
        private String profileImg;
        private String website;
        private int postCount;
        private int followerCount;
        private int followingCount;
        private String followStatus;
        private List<GetProfiletImgRes> profilePostImgs;

}
