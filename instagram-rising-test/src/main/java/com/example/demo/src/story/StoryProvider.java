package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.story.model.GetStoriesByUserRes;
import com.example.demo.src.story.model.GetStoriesRes;
import com.example.demo.src.story.model.GetUserListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoryProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StoryDao storyDao;

    private final JwtService jwtService;

    @Autowired
    public StoryProvider(StoryDao storyDao, JwtService jwtService) {
        this.storyDao = storyDao;
        this.jwtService = jwtService;
    }

    public GetStoriesRes getStories(int stroyIdx) throws BaseException {
        try {
            GetStoriesRes getStoriesRes = storyDao.getStories(stroyIdx);
            return getStoriesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetStoriesByUserRes> getStoriesByUser(int userIdx) throws BaseException {
        try {
            List<GetStoriesByUserRes> getStoriesByUser = storyDao.getStoriesByUser(userIdx);
            return getStoriesByUser;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLike(int userIdx, int storyIdx) throws BaseException{
        try{
            return storyDao.checkLike(userIdx, storyIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserListRes getUserList(int stroyIdx) throws BaseException {
        try {
            GetUserListRes getUserListRes = storyDao.getUserList(stroyIdx);
            return getUserListRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserBehindStatus(int userIdx, int followingIdx) throws BaseException {
        try {
            int result = storyDao.checkUserBehindStatus(userIdx, followingIdx);
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
