package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus2 {
    /**
     * 1000 : 요청 성공
     */


    /**
     * 2000 : Request 오류
     */
    // Common

    // [POST] /posts
    POST_POST_CONTENT_EMPTY(false, 2050, "게시글 글 값이 없습니다."),



    /**
     * 3000 : Response 오류
     */
    // Common




    /**
     * 4000 : Database, Server 오류
     */
;


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus2(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
