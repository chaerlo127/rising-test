package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.BaseResponseStatus2;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;

    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postService = postService;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
    }

    // 게시물 작성 api
    @PostMapping("/{userIdx}")
    @ResponseBody
    public BaseResponse<String> createPost(@PathVariable("userIdx") int userIdx, @RequestBody PostReq postReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(postReq.getContent() == null){
                return new BaseResponse<>(BaseResponseStatus2.POST_POST_CONTENT_EMPTY);
            }
            int postIdx =this.postService.createPost(userIdx, postReq);
            if(!postReq.getPostImgReqs().isEmpty()){
                this.postService.createPostImg(postReq, postIdx);
            }
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 게시물 조회 api
    @GetMapping("/{postIdx}")
    @ResponseBody
    public BaseResponse<GetPostRes> getPostByPostIdx(@PathVariable("postIdx") int postIdx){
        try{
            return new BaseResponse<>(this.postProvider.getPostByPostIdx(postIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 게시물 수정 api
    @PatchMapping("")
    @ResponseBody
    public BaseResponse<String> updatePostsByPostIdxAndUserIdx(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx,
                                                               @RequestBody PatchReq patchReq){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            this.postService.updatePostsByPostIdxAndUserIdx(postIdx, userIdx, patchReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    // 게시물 삭제 api
    @DeleteMapping("")
    @ResponseBody
    public BaseResponse<String> deletePostByPostIdxAndUserIdx(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            this.postService.deletePostByPostIdxAndUserIdx(postIdx, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 게시물 피드 조회
    @GetMapping("/feeds")
    @ResponseBody
    public BaseResponse<List<GetFeedRes>> getFeed(){
        /**
         *  jwt validation 확인 이전
         */
        try{
            return new BaseResponse<>(this.postProvider.getFeed());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 게시물 좋아요 유저 리스트
    @GetMapping("/likes")
    @ResponseBody
    public BaseResponse<List<GetPostLikeListRes>> getPostLikeList(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postProvider.getPostLikeList(postIdx, userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 게시물 좋아요
    // 좋아요 = true, 좋아요 취소 = false
    @PatchMapping("/likes")
    @ResponseBody
    public BaseResponse<Boolean> createPostLike(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postService.createPostLike(postIdx, userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }
    
    // 알고리즘 랜덤 피드 조회
    @GetMapping("/randFeeds")
    @ResponseBody
    public BaseResponse<List<GetRandFeedListRes>> getRanFeedList(){
        try{
            return new BaseResponse<>(this.postProvider.getRanFeedList());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 랜덤 피드 조회에서 클릭한 게시물의 피드들
    @GetMapping("/randFeeds/{postIdx}")
    @ResponseBody
    public BaseResponse<List<GetRandFeedRes>> getRandPostAndFeeds(@PathVariable("postIdx") int postIdx){
        /**
         *  jwt validation 확인 이전
         */
        try{
            return new BaseResponse<>(this.postProvider.getRandPostAndFeeds(postIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 게시물 신고하기
    @PostMapping("/reports")
    @ResponseBody
    public BaseResponse<String> postReportPost(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx,
                                               @RequestBody PostReportReq postReportReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postReportReq.getReason() == null || postReportReq.getReason().equals("")){
                return new BaseResponse<>(WRITE_POST_REPORT_REASON);
            }
            this.postService.postReportPost(userIdx, postIdx, postReportReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    
    // 사용자가 좋아요 누른 게시물 리스트 [최신순]
    @GetMapping("/likes/recentLists/{userIdx}")
    @ResponseBody
    public BaseResponse<List<GetUserLikeFeedsListRes>> getUserLikesRecentList(@PathVariable("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postService.getUserLikesRecentList(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 사용자가 좋아요 누른 게시물 리스트 [오래된 순]
    @GetMapping("/likes/pastLists/{userIdx}")
    @ResponseBody
    public BaseResponse<List<GetUserLikeFeedsListRes>> getUserLikesPastList(@PathVariable("userIdx") int userIdx){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postService.getUserLikesPostList(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 게시물 저장
    // true: 저장 false; 저장 취소
    @PostMapping("/saving")
    @ResponseBody
    public BaseResponse<Boolean> createPostSave(@RequestParam("postIdx") int postIdx, @RequestParam("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postService.createPostSave(postIdx, userIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 게시물 저장 리스트
    @GetMapping("/saving")
    @ResponseBody
    public BaseResponse<List<GetUserSaveFeedsListRes>> getPostSaveList(@RequestParam("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(this.postService.getPostSaveList(userIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
