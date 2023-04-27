package com.example.demo.src.profile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.profile.model.GetPersonInfo;
import com.example.demo.src.profile.model.PatchProfileReq;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProfileService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileDao profileDao;
    private final JwtService jwtService;
    private final UserDao userDao;
    private final ProfileProvider profileProvider;
    public ProfileService(ProfileDao profileDao,  JwtService jwtService, UserDao userDao, ProfileProvider profileProvider) {
        this.profileDao = profileDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.profileProvider = profileProvider;
    }
    public void patchUserInfoByUserIdx(int userIdx, GetPersonInfo getPersonInfo) throws BaseException {
        GetPersonInfo user = this.userDao.getUserInfoByUserIdx(userIdx);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        if (this.userDao.patchUserInfoByUserIdx(userIdx, getPersonInfo) == 0) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void updateUsersByUserIdx(int userIdx, PatchProfileReq patchProfileReq) throws BaseException{
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        if(this.profileProvider.checkUserIdByUserIdx(patchProfileReq.getUserId(), userIdx) == 1){
            throw new BaseException(BaseResponseStatus.PATCH_PROFILES_EXISTS_USERID);
        }
        try{
            this.profileDao.updateUsersByUserIdx(userIdx, patchProfileReq);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
