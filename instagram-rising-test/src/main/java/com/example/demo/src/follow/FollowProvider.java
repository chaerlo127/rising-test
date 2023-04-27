package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.GetFollowsRes;
import com.example.demo.utils.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowProvider {

    private final FollowDao followDao;
    private final JwtService jwtService;

    public FollowProvider(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public List<GetFollowsRes> getFollowers(int userIdx) throws BaseException {
        try{
            List<GetFollowsRes> getFollowsRes = followDao.getFollowers(userIdx);
            return getFollowsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowsRes> getFollowings(int userIdx) throws BaseException {
        try{
            List<GetFollowsRes> getFollowsRes = followDao.getFollowings(userIdx);
            return getFollowsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFollow(int userIdx, String followIdx) throws BaseException{
        try{
            return followDao.checkFollow(userIdx, followIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowsRes> getRecommends(int userIdx) throws BaseException {
        try{
            List<GetFollowsRes> getFollowsRes = followDao.getRecommends(userIdx);
            return getFollowsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowsRes> getTogether(int userIdx, int followIdx) throws BaseException {
        try{
            List<GetFollowsRes> getFollowsRes = followDao.getTogether(userIdx, followIdx);
            return getFollowsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
