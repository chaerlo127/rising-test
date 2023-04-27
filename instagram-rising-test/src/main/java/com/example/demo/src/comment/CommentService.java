package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.*;
import com.example.demo.src.post.PostDao;
import com.example.demo.src.post.model.GetPostReportRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.PostReportReq;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final JwtService jwtService;
    private final CommentDao commentDao;
    private final PostDao postDao;
    private final UserDao userDao;

    @Autowired
    public CommentService(JwtService jwtService, CommentDao commentDao, PostDao postDao, UserDao userDao){
        this.jwtService = jwtService;
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.userDao = userDao;
    }

    public void createComment(int postIdx, int userIdx, PostCommentReq postCommentReq) throws BaseException {
        this.validationForPostingUser(postIdx, userIdx);
        try{
            this.commentDao.createComment(postIdx, userIdx, postCommentReq);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private void validationForPostingUser(int postIdx, int userIdx) throws BaseException {
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        GetPostRes postRes = this.postDao.getPostByPostIdx(postIdx);
        // post를 찾을 수 없다면
        if(postRes == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_POSTS);
        }
    }

    public void deleteComment(int commentIdx, int userIdx) throws BaseException{
        GetCommentRes comment = this.validationCommentForPostingUser(commentIdx, userIdx);
        // comment를 작성한 유저가 아니라면?
        if(comment.getUserIdx() != userIdx){
            throw new BaseException(BaseResponseStatus.DELETE_COMMENT_USER_IS_NOT_SAME_ACCESS_USER);
        }
        try{
            this.commentDao.deleteComment(commentIdx, userIdx);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private GetCommentRes validationCommentForPostingUser(int commentIdx, int userIdx) throws BaseException {
        // user를 찾을 수 없다면
        if(this.userDao.getUserByUserIdx(userIdx) == null){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_USERS);
        }
        // comment를 찾을 수 없다면?
        GetCommentRes comment = this.commentDao.getCommentByCommentIdx(commentIdx);
        if(comment == null){
            throw new BaseException(BaseResponseStatus.DELETE_COMMENT_IS_NULL);
        }
        return comment;
    }

    public boolean patchCommentLike(int commentIdx, int userIdx) throws BaseException {
        validationCommentForPostingUser(commentIdx, userIdx);
        // db 안에 저장이 되어 있는지
        GetCommentLikeRes likes = this.commentDao.getCommentLikeByCommentIdxAndUserIdx(commentIdx, userIdx);

        // 저장이 안되면 추가 후 true
        if (likes == null) {
            if(this.commentDao.createCommentLike(commentIdx, userIdx) == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        } else if (likes.getStatus().equals("INACTIVE")) {
            // db 안 inactive 인지? => active로 바꾸고 true
            if(this.commentDao.patchCommentLike(commentIdx, userIdx, "ACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return true;
        } else {
            // active일 경우 inactive로 바꾸고 false
            if(this.commentDao.patchCommentLike(commentIdx, userIdx, "INACTIVE") == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
            return false;
        }
    }

    public void postReportPost(int userIdx, int commentIdx, PostCommentReportReq postCommentReportReq) throws BaseException{
        this.validationCommentForPostingUser(commentIdx, userIdx);
        GetCommentReportRes report = this.commentDao.getCommentReportByUserIdxCommentIdx(commentIdx, userIdx);
        if(report == null){
            if(this.commentDao.createCommentReport(commentIdx, userIdx, postCommentReportReq) == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        }else {
            throw new BaseException(BaseResponseStatus.ALREADY_POST_REPORT);
        }

    }
}
