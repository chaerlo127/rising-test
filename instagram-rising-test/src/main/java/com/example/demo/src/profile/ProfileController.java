package com.example.demo.src.profile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.profile.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProfileProvider profileProvider;
    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final JwtService jwtService;

    public ProfileController(ProfileProvider profileProvider, ProfileService profileService, JwtService jwtService) {
        this.profileProvider = profileProvider;
        this.profileService = profileService;
        this.jwtService = jwtService;
    }

    // 프로필 편집 조회
    @ResponseBody
    @GetMapping("/edit/{userIdx}") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<GetProfileEditRes> getUsersByUserIdx(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Users
            GetProfileEditRes getProfileRes = profileProvider.getUsersByUserIdx(userIdx);
            return new BaseResponse<>(getProfileRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 프로필 편집
    @ResponseBody
    @PatchMapping("/edit/{userIdx}") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<String> updateUsersByUserIdx(@PathVariable("userIdx") int userIdx, @RequestBody PatchProfileReq patchProfileReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Users
            profileService.updateUsersByUserIdx(userIdx, patchProfileReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 프로필 조회
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<GetProfileUserRes> getProfileUserByUserIdx(@RequestParam("profileIdx") int profileIdx, @RequestParam("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Users
            return new BaseResponse<>( profileProvider.getProfileUserByUserIdx(profileIdx, userIdx));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 개인 정보 설정 조회 API
    @ResponseBody
    @GetMapping("/userInfo/{userIdx}")
    public BaseResponse<GetPersonInfo> getUserInfoByUserIdx(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.profileProvider.getUserInfoByUserIdx(userIdx));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 개인 정보 설정 변경 API
    @ResponseBody
    @PatchMapping("/userInfo/{userIdx}")
    public BaseResponse<String> patchUserInfoByUserIdx(@PathVariable("userIdx") int userIdx, @RequestBody GetPersonInfo getPersonInfo) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            this.profileService.patchUserInfoByUserIdx(userIdx, getPersonInfo);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    // 이  게정 정보
    @ResponseBody
    @GetMapping("/info/{userIdx}")
    public BaseResponse<GetProfileInfo> getProfileInfoByUserIdx(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.profileProvider.getProfileInfoByUserIdx(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
