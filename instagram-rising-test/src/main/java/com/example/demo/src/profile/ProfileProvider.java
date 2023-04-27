package com.example.demo.src.profile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.profile.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProfileProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileDao profileDao;
    private final UserDao userDao;
    private final JwtService jwtService;
    private final FollowDao followDao;
    public ProfileProvider(ProfileDao profileDao, UserDao userDao, JwtService jwtService, FollowDao followDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.followDao = followDao;
    }

    public GetProfileEditRes getUsersByUserIdx(int userIdx) throws BaseException {
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        GetProfileEditRes user = this.profileDao.getUsersByUserIdx(userIdx);
        if(user == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        return user;
    }

    public int checkUserIdByUserIdx(String userId, int userIdx) throws BaseException{
        try{
            return profileDao.checkUserId(userId, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetProfileUserRes getProfileUserByUserIdx(int profileIdx, int userIdx) throws BaseException {
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        if(this.userDao.getUserByUserIdx(profileIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_PROFILE_USERS);
        }
        try{
            GetProfileUserRes profile = this.profileDao.getProfileUserByUserIdx(profileIdx);
            if(userIdx != profileIdx && this.followDao.checkFollow(userIdx, String.valueOf(profileIdx)) == 1){
                profile.setFollowStatus(this.followDao.getFollowStatus(profileIdx, userIdx).getFollowStatus());
            }else if(userIdx != profileIdx){
                profile.setFollowStatus("INACTIVE");
            }
            return profile;
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public GetPersonInfo getUserInfoByUserIdx(int userIdx) throws BaseException {
        GetPersonInfo user = this.userDao.getUserInfoByUserIdx(userIdx);
        if(user == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        return user;
    }


    public GetProfileInfo getProfileInfoByUserIdx(int userIdx) throws BaseException{
        GetProfileInfo user = this.userDao.getProfileInfoByUserIdx(userIdx);
        if(user == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        return user;
    }
}
