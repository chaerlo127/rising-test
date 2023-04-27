package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.src.story.model.PostStoryReq;
import com.example.demo.src.story.model.PostStoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoryService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoryDao storyDao;

    private final StoryProvider storyProvider;

    private final JwtService jwtService;

    @Autowired
    public StoryService(StoryDao storyDao, StoryProvider storyProvider, JwtService jwtService) {
        this.storyDao = storyDao;
        this.storyProvider = storyProvider;
        this.jwtService = jwtService;
    }

    public PostStoryRes createStory(int userIdx, PostStoryReq postStoryReq) throws BaseException {
        try {
            int storyIdx = storyDao.createStoryIdx(userIdx, postStoryReq);
            storyDao.createStoryByStoryIdx(postStoryReq, storyIdx);
            PostStoryRes postStoryRes = new PostStoryRes(storyIdx, userIdx);
            return postStoryRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteStoryByUser(int storyIdx) throws BaseException {
        try {
            storyDao.deleteStoryByUser(storyIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteStory() throws BaseException {
        try {
            storyDao.deleteStory();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public PostStoryRes likeStories(int userIdx, int storyIdx) throws BaseException {
        try {
            storyDao.likeStories(userIdx, storyIdx);
            PostStoryRes postStoryRes = new PostStoryRes(storyIdx, userIdx);
            return postStoryRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostStoryRes dislikeStories(int userIdx, int storyIdx, String status) throws BaseException {
        try {
            storyDao.dislikeStories(userIdx, storyIdx, status);
            PostStoryRes postStoryRes = new PostStoryRes(storyIdx, userIdx);
            return postStoryRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void reportStories(int userIdx, int storyIdx, String content) throws BaseException {
        try {
            storyDao.reportStories(userIdx, storyIdx, content);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createViewer(int userIdx, int storyIdx) throws BaseException {
        try {
            storyDao.createViewer(userIdx, storyIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void behindStory(int userIdx, int followingIdx) throws BaseException {
        try {
            storyDao.behindStory(userIdx, followingIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void viewStory(int userIdx, int followingIdx) throws BaseException {
        try {
            storyDao.viewStory(userIdx, followingIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
