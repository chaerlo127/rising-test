package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userId;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String birth;
    private String contract1; //이용약관 동의
    private String contract2; //개인정보 동의
    private String contract3; //위치 기반 기능
}
