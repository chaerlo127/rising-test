package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 입력해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_USER_ID(false, 2011, "사용자 이름 값을 입력해주세요."),
    POST_USERS_EMPTY_USER_PASSWORD(false, 2012, "비밀번호 값을 입력해주세요."),
    POST_USERS_EMPTY_PHONE(false, 2013, "휴대폰번호를 입력해주세요."),
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PHONE(false, 2017, "휴대폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2018,"중복된 이메일입니다."),
    POST_USERS_EXISTS_PHONE(false,2019,"중복된 휴대폰 번호입니다."),
    POST_USERS_EXISTS_USERID(false,2020, "중복된 사용자 이름입니다."),
    POST_USERS_EMPTY_CONTRACT(false,2021,"필수 약관에 동의해주세요."),
    POST_USERS_EMPTY_BIRTH(false,2023,"생년월일을 입력해주세요."),
    POST_USERS_FAIL_TO_JOIN(false,2024,"가입에 실패했습니다."),

    //[PATCH] /users/reset/password
    PATCH_USERS_PASSWORD_EMAIL_PHONE(false,2025,"이메일 혹은 휴대폰번호를 입력해주세요."),

    // [POST] /users/login
    POST_USERS_LOGIN_EMPTY_EMAIL_PHONE(false, 2022, "이메일 혹은 휴대폰번호를 입력해주세요."),

    // [POST] /stories
    POST_STORIES_EMPTY_CONTENT(false, 2030, "스로리 이미지를 넣어주세요."),

    // [POST/PATCH] /posts
    WRITE_POST_REPORT_REASON(false, 2068, "신고 이유를 작성해주세요."),
    ALREADY_POST_REPORT(false, 2069, "이미 신고한 글입니다."),
    DO_NOT_HAVE_USERS(false, 2070, "유저가 확인되지 않습니다."),
    DO_NOT_HAVE_PROFILE_USERS(false, 2073, "프로필 유저가 확인되지 않습니다."),
    DO_NOT_HAVE_POSTS(false, 2071, "게시물이 확인되지 않습니다."),
    POSTING_USER_NOT_EQUAL_ACCESS_USER(false, 2072, "접근 유저와 게시글의 유저가 같지 않습니다."),
    DELETE_POSTS_IS_NULL(false,2073, "게시글 번호가 유효하지 않습니다."),

    // [PATCH]  /profiles
    PATCH_PROFILES_EXISTS_USERID(false,2080, "중복된 사용자 이름입니다."),


    // [POST]  /COMMENTS
    POST_REPLY_IS_NULL(false,2085, "댓글의 값이 없습니다."),

    // [PATCH]  /COMMENTS
    DELETE_COMMENT_IS_NULL(false,2088, "댓글 번호가 유효하지 않습니다."),
    DELETE_COMMENT_USER_IS_NOT_SAME_ACCESS_USER(false,2089, "댓글을 작성한 유저와 다른 유저입니다."),

    //[POST] /follows
    POST_EMPTY_FOLLOW_ID(false, 2090, "팔로우 유저를 입력해주세요."),

    //[GET] /searches
    SEARCH_EMPTY_CONDITION(false, 2091, "상태값을 확인해주세요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    INACTIVE_USER(false,3015,"탈퇴 회원입니다."),

    //[GET] /stories
    FAILED_TO_STORIES(false,3020,"스토리를 불러오는데 실패하였습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    FAILED_TO_WITHDRAW(false,4013,"회원 탈퇴에 실패하였습니다.");





    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
