package com.example.demo.src.profile.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileEditRes {
        private int userIdx;
        private String userId;
        private String name;
        private String profileImg;
        private String website;
        private String introduction;
}
