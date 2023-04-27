package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserIdReq {
    private String email;
    private String phone;
    private String password;
    private String newUserId;
}
