package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        if (postUserReq.getEmail().equals("")) postUserReq.setEmail(null);
        if (postUserReq.getPhone().equals("")) postUserReq.setPhone(null);
        //이메일중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //휴대폰번호 중복
        if(userProvider.checkPhone(postUserReq.getPhone()) ==1){
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }
        //아이디 중복
        if(userProvider.checkUserId(postUserReq.getUserId()) ==1){
            throw new BaseException(POST_USERS_EXISTS_USERID);
        }
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void withdrawUser(int userIdx) throws BaseException {
        try {
            int result = userDao.withdrawUser(userIdx);
            if(result == 0){
                throw new BaseException(FAILED_TO_WITHDRAW);
            }
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_WITHDRAW);
        }
    }

    public void modifyUserLoginStatus(PostLoginReq postLoginReq, String loginStatus) throws BaseException {
        try{
            int userIdx = userDao.getUserIdxByEmailOrPhone(postLoginReq.getEmail(), postLoginReq.getPhone());
            int result = userDao.modifyUserLoginStatus(userIdx, loginStatus);
            if(result == 0){
                throw new BaseException(DATABASE_ERROR);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes resetUserPassword(PatchUserReq patchUserReq) throws BaseException {
        try{
            String pwd;
            try{
                //암호화
                pwd = new SHA256().encrypt(patchUserReq.getNewPassword());
                patchUserReq.setNewPassword(pwd);

            } catch (Exception ignored) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }
            try{
                userDao.resetUserPassword(patchUserReq);
                int userIdx = userDao.getUserIdxByEmailOrPhone(patchUserReq.getEmail(), patchUserReq.getPhone());
                //jwt 발급.
                String jwt = jwtService.createJwt(userIdx);
                return new PatchUserRes(userIdx, jwt);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes modifyUserId(int userIdx, PatchUserIdReq patchUserIdReq) throws BaseException {
        try{
            userDao.modifyUserId(userIdx, patchUserIdReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PatchUserRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
