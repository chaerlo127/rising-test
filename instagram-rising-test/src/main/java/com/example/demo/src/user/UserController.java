package com.example.demo.src.user;

import com.example.demo.config.BaseResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
            if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        } else if (postUserReq.getPhone() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        } else if (postUserReq.getUserId() == null) {
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        } else if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_USER_PASSWORD);
        } else if (postUserReq.getBirth() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTH);
        } else if (postUserReq.getContract1() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_CONTRACT);
        } else if (postUserReq.getContract2() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_CONTRACT);
        } else if (postUserReq.getContract3() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_CONTRACT);
        } else if (postUserReq.getEmail().equals("") && postUserReq.getPhone().equals("")) {
            return new BaseResponse<>(POST_USERS_FAIL_TO_JOIN);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //휴대폰번호 정규표현
        if(!isRegexPhone(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        //만 13세 미만 가입 금지
        if(!isRegexBirth(postUserReq.getBirth())) {
            return new BaseResponse<>(POST_USERS_FAIL_TO_JOIN);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원탈퇴 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<BaseResponseStatus>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<BaseResponseStatus> withdrawUser(@PathVariable("userIdx") int userIdx){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //TODO:게시글, 좋아요, 댓글 완성 시 함께 삭제
            userService.withdrawUser(userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            //validatin
            if (postLoginReq.getEmail() == null && postLoginReq.getPhone() == null) {
                return new BaseResponse<>(POST_USERS_LOGIN_EMPTY_EMAIL_PHONE);
            } else if (postLoginReq.getPassword() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_USER_PASSWORD);
            }
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            //로그인
            userService.modifyUserLoginStatus(postLoginReq, "ACTIVE");
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그아웃 API
     * [Post] /users/logOut
     */
    @ResponseBody
    @PostMapping("/{userIdx}/logOut")
    public BaseResponse<BaseResponseStatus> logOut(@PathVariable("userIdx") int userIdx, @RequestBody PostLoginReq postLoginReq){
        try{
            //validatin
            if (postLoginReq.getEmail() == null && postLoginReq.getPhone() == null) {
                return new BaseResponse<>(POST_USERS_LOGIN_EMPTY_EMAIL_PHONE);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //로그아웃
            userService.modifyUserLoginStatus(postLoginReq, "INACTIVE");
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 변경 API
     * [PATCH] /users/:userIdx/password/reset
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/password/reset")
    public BaseResponse<PatchUserRes> resetUserPassword(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq){
        try {
            if(patchUserReq.getUserId() == null){
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            } else if (patchUserReq.getPhone() == null && patchUserReq.getEmail() == null) {
                return new BaseResponse<>(PATCH_USERS_PASSWORD_EMAIL_PHONE);
            } else if (patchUserReq.getNewPassword() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_USER_PASSWORD);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchUserRes patchUserRes = userService.resetUserPassword(patchUserReq);

        return new BaseResponse<>(patchUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 아이디 변경 API
     * [PATCH] /users/:userIdx/userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/userId")
    public BaseResponse<PatchUserRes> resetUserPassword(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserIdReq patchUserIdReq){
        try {
            if(patchUserIdReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_USER_PASSWORD);
            } else if (patchUserIdReq.getPhone() == null && patchUserIdReq.getEmail() == null) {
                return new BaseResponse<>(PATCH_USERS_PASSWORD_EMAIL_PHONE);
            } else if (patchUserIdReq.getNewUserId() == null) {
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetUserRes> getUserRes = userProvider.getUsers();
            for (int i = 0; i < getUserRes.size(); i++) {
                if (getUserRes.get(i).getUserId().equals(patchUserIdReq.getNewUserId())) {
                    return new BaseResponse<>(POST_USERS_EXISTS_USERID);
                }
            }

            PatchUserRes patchUserRes = userService.modifyUserId(userIdx, patchUserIdReq);

            return new BaseResponse<>(patchUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
