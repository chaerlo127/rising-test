package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.GetCommentLikeListRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentProvider {
    private final JwtService jwtService;
    private final CommentDao commentDao;

    @Autowired
    public CommentProvider(JwtService jwtService, CommentDao commentDao){
        this.jwtService = jwtService;
        this.commentDao = commentDao;
    }

    public List<GetCommentLikeListRes> getCommentLikeList(int commentIdx, int userIdx) throws BaseException {
        if(this.commentDao.getCommentByCommentIdx(commentIdx) == null){
            throw new BaseException(BaseResponseStatus.DELETE_COMMENT_IS_NULL);
        }
        try{
            return this.commentDao.getCommentLikeList(commentIdx, userIdx);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
