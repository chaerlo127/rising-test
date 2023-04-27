package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.GetCommentLikeListRes;
import com.example.demo.src.comment.model.PostCommentReportReq;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.src.post.model.PostReportReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final JwtService jwtService;
    private final CommentProvider commentProvider;
    private final CommentService commentService;

    @Autowired
    public CommentController(JwtService jwtService, CommentProvider commentProvider, CommentService commentService){
        this.jwtService = jwtService;
        this.commentProvider = commentProvider;
        this.commentService = commentService;
    }

    @PostMapping("")
    @ResponseBody
    public BaseResponse<String> createComment(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx,
                                              @RequestBody PostCommentReq postCommentReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postCommentReq.getReply() == null){
                return new BaseResponse<>(POST_REPLY_IS_NULL);
            }
            this.commentService.createComment(postIdx, userIdx, postCommentReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @PatchMapping("")
    @ResponseBody
    public BaseResponse<String> deleteComment(@RequestParam("commentIdx") int commentIdx, @RequestParam("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            this.commentService.deleteComment(commentIdx, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 댓글 좋아요
    // 좋아요 = true, 좋아요 취소 = false
    @PatchMapping("/likes")
    @ResponseBody
    public BaseResponse<Boolean> patchCommentLike(@RequestParam("commentIdx") int commentIdx, @RequestParam("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.commentService.patchCommentLike(commentIdx, userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 댓글 좋아요 리스트
    @GetMapping("/likes")
    @ResponseBody
    public BaseResponse<List<GetCommentLikeListRes>> getCommentLikeList(@RequestParam("commentIdx") int commentIdx, @RequestParam("userIdx") int userIdx){
        try{
            return new BaseResponse<>(this.commentProvider.getCommentLikeList(commentIdx, userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 댓글 신고하기
    @PostMapping("/reports")
    @ResponseBody
    public BaseResponse<String> postReportPost(@RequestParam("commentIdx") int commentIdx, @RequestParam("userIdx") int userIdx,
                                               @RequestBody PostCommentReportReq postCommentReportReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postCommentReportReq.getReason() == null || postCommentReportReq.getReason().equals("")){
                return new BaseResponse<>(WRITE_POST_REPORT_REASON);
            }
            this.commentService.postReportPost(userIdx, commentIdx, postCommentReportReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
