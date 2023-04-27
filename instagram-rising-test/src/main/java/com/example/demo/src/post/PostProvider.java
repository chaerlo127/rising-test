package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostProvider {
    private final PostDao postDao;
    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService, UserDao userDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.jwtService = jwtService;
    }

    public GetPostRes getPostByPostIdx(int postIdx) throws BaseException {
        GetPostRes user = this.postDao.getPostByPostIdx(postIdx);
        if(user == null) throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_POSTS);
        else return user;
    }

    public List<GetFeedRes> getFeed() throws BaseException{
        try{
            return this.postDao.getFeed();
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public List<GetPostLikeListRes> getPostLikeList(int postIdx, int userIdx) throws BaseException {
        try {
            return this.postDao.getPostLikeList(postIdx, userIdx);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetRandFeedListRes> getRanFeedList() throws BaseException{
        try{
            return this.postDao.getRanFeedList();
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public List<GetRandFeedRes> getRandPostAndFeeds(int postIdx) throws BaseException {
        try{
            List<GetRandFeedRes> getRandFeeds = this.postDao.getFeedRand(postIdx);
            getRandFeeds.addAll(this.postDao.getPostForRand(postIdx));
            return getRandFeeds;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void validationForPostingUser(int postIdx, int userIdx) throws BaseException {
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        GetPostRes postRes = this.postDao.getPostByPostIdx(postIdx);
        // post를 찾을 수 없다면
        if(postRes == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_POSTS);
        }
        // post를 작성한 user가 아니라면
        if(postRes.getUserIdx() != userIdx){
            throw new BaseException(BaseResponseStatus.POSTING_USER_NOT_EQUAL_ACCESS_USER);
        }
    }

    public GetPostRes validationPostForPostingUser(int postIdx, int userIdx) throws BaseException {
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        // 게시글을 찾을 수 없다면?
        GetPostRes post = this.postDao.getPostByPostIdx(postIdx);
        if(post == null){
            throw new BaseException(BaseResponseStatus.DELETE_POSTS_IS_NULL);
        }
        return post;
    }
}
