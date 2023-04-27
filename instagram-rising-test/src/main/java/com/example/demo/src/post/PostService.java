package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.BaseResponseStatus2;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.post.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostProvider postProvider;
    private final PostDao postDao;
    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostService(PostProvider postProvider, PostDao postDao, JwtService jwtService, UserDao userDao) {
        this.postProvider = postProvider;
        this.postDao = postDao;
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int createPost(int userIdx, PostReq postReq) throws BaseException {
        try{
            return this.postDao.createPost(userIdx, postReq);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }



    public void createPostImg(PostReq postReq, int postIdx) throws BaseException {
        try{
            this.postDao.saveAll(postReq, postIdx);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void updatePostsByPostIdxAndUserIdx(int postIdx, int userIdx, PatchReq patchReq) throws BaseException {
        this.postProvider.validationForPostingUser(postIdx, userIdx);
        try {
            this.postDao.updatePostsByPostIdxAndUserIdx(postIdx, userIdx, patchReq); // 내용 변경
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }


    public void deletePostByPostIdxAndUserIdx(int postIdx, int userIdx) throws BaseException {
        this.postProvider.validationForPostingUser(postIdx, userIdx);
        try{
            // 게시글 좋아요 삭제
            this.postDao.deletePostLikeByPostIdxAndUserIdx(postIdx);
            // 게시글 댓글 삭제
            this.postDao.deletePostCommentByPostIdxAndUserIdx(postIdx);
            // 게시글 이미지 삭제
            this.postDao.deletePostImgByPostIdxAndUserIdx(postIdx);
            // 게시물 삭제
            this.postDao.deletePostByPostIdxAndUserIdx(postIdx, userIdx);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }





    public Boolean createPostLike(int postIdx, int userIdx) throws BaseException {
        this.postProvider.validationPostForPostingUser(postIdx, userIdx);
        GetPostLikeRes likes = this.postDao.getPostLikeByPostIdxAndUserIdx(postIdx, userIdx);
        if(likes == null){
            if(this.postDao.createPostLike(postIdx, userIdx) ==0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        }else if(likes.getStatus().equals("ACTIVE")){ // 현재 좋아요가 눌린 경우
            if(this.postDao.patchPostLike(postIdx, userIdx, "INACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return false;
        }else { // 현재 좋아요가 눌리지 않은 경우
            if(this.postDao.patchPostLike(postIdx, userIdx, "ACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        }
    }



    public void postReportPost(int userIdx, int postIdx, PostReportReq postReportReq) throws BaseException {
        this.postProvider.validationPostForPostingUser(postIdx, userIdx);
        GetPostReportRes report = this.postDao.getPostReportByUserIdxPostIdx(postIdx, userIdx);
        if(report == null){
            if(this.postDao.createPostReport(postIdx, userIdx, postReportReq) ==0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        }else {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_REPORT);
        }
    }

    public List<GetUserLikeFeedsListRes> getUserLikesRecentList(int userIdx) throws BaseException{
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        try{
            return this.postDao.getUserLikeFeedsList(userIdx, this.postDao.userLikeFeedListRecent());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetUserLikeFeedsListRes> getUserLikesPostList(int userIdx) throws BaseException{
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        try{
            return this.postDao.getUserLikeFeedsList(userIdx, this.postDao.userLikeFeedListPast());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public boolean createPostSave(int postIdx, int userIdx) throws BaseException{
        this.postProvider.validationPostForPostingUser(postIdx, userIdx);
        GetPostSaveRes saves = this.postDao.getPostSaveByPostIdxAndUserIdx(postIdx, userIdx);
        if(saves == null){
            if(this.postDao.createPostSave(postIdx, userIdx)==0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        }else if(saves.getStatus().equals("ACTIVE")){
            if(this.postDao.patchPostSave(postIdx, userIdx, "INACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return false;
        }else{
            if(this.postDao.patchPostSave(postIdx, userIdx, "ACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        }
    }

    public List<GetUserSaveFeedsListRes> getPostSaveList(int userIdx) throws BaseException{
        try{
            return this.postDao.getUserSavesFeedsList(userIdx);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
