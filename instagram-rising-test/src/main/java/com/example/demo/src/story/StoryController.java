package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.story.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/stories")
public class StoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoryService storyService;

    @Autowired
    private final StoryProvider storyProvider;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserProvider userProvider;

    public StoryController(StoryService storyService, StoryProvider storyProvider, JwtService jwtService, UserProvider userProvider) {
        this.storyService = storyService;
        this.storyProvider = storyProvider;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 스토리 조회 API
     * [GET] /stories/:userIdx/:storyIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}/{storyIdx}")
    public BaseResponse<GetStoriesRes> getStories(@PathVariable("userIdx") int userIdx, @PathVariable("storyIdx") int storyIdx) {
        try {
            // Get Stories
            GetStoriesRes getStoriesRes = storyProvider.getStories(storyIdx);
            if (getStoriesRes.getGetStoriesByStoryIdxRes().get(0).getStoryImg() == null) {
                return new BaseResponse<>(FAILED_TO_STORIES);
            }
            // Put Viewer
            storyService.createViewer(userIdx, storyIdx);
            return new BaseResponse<>(getStoriesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 스토리 전체 조회 API
     * [GET] /stories/:userIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetStoriesByUserRes>> getStoriesByUser(@PathVariable("userIdx") int userIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Stories
            List<GetStoriesByUserRes> getStoriesByUserRes = storyProvider.getStoriesByUser(userIdx);
            return new BaseResponse<>(getStoriesByUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 작성 API
     * [POST] /stories/:userIdx
     * @return BaseResponse<GetStoriesRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostStoryRes> createStory(@PathVariable("userIdx") int userIdx, @RequestBody PostStoryReq postStoryReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postStoryReq.getImg() == null) {
                return new BaseResponse<>(POST_STORIES_EMPTY_CONTENT);
            }
            // Create Stories
            PostStoryRes postStoryRes = storyService.createStory(userIdx, postStoryReq);
            return new BaseResponse<>(postStoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 삭제 API
     * [PATCH] /stories/:userIdx/:storyIdx
     * 24시간 지난 스토리 삭제 API
     * [PATCH] /stories/:userIdx/:storyIdx ? expiration =
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/{storyIdx}")
    public BaseResponse<BaseResponseStatus> deleteStory(@PathVariable("userIdx") int userIdx, @PathVariable("storyIdx") int storyIdx, @RequestParam(required = false) String expiration){
        try {
            if (expiration == null){
                //jwt에서 idx 추출.
                int userIdxByJwt = jwtService.getUserIdx();
                //userIdx와 접근한 유저가 같은지 확인
                if (userIdx != userIdxByJwt) {
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
                storyService.deleteStoryByUser(storyIdx);
            } else {
                storyService.deleteStory();
            }
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 좋아요 API
     * [POST] /stories/likes/:userIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @PostMapping("/likes/{userIdx}")
    public BaseResponse<PostStoryRes> likeStories(@PathVariable("userIdx") int userIdx, @RequestBody PostLikeStoryReq postLikeStoryReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Stories
            GetStoriesRes getStoriesRes = storyProvider.getStories(postLikeStoryReq.getStoryIdx());
            if (getStoriesRes.getGetStoriesByStoryIdxRes().get(0).getStoryImg() == null) {
                return new BaseResponse<>(FAILED_TO_STORIES);
            }
            PostStoryRes postStoryRes;
            int likeResult = storyProvider.checkLike(userIdx, postLikeStoryReq.getStoryIdx());
            if (likeResult == 0) {
                // Like Stories
                postStoryRes = storyService.likeStories(userIdx, postLikeStoryReq.getStoryIdx());
            } else if (likeResult != 0) {
                if (!postLikeStoryReq.getStatus().equals("ACTIVE") && !postLikeStoryReq.getStatus().equals("INACTIVE")) {
                    return new BaseResponse<>(REQUEST_ERROR);
                }
                // Like Stories
                postStoryRes = storyService.dislikeStories(userIdx, postLikeStoryReq.getStoryIdx(), postLikeStoryReq.getStatus());
            } else {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(postStoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 신고 API
     * [POST] /stories/reports/:userIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @PostMapping("/reports/{userIdx}")
    public BaseResponse<BaseResponseStatus> reportStories(@PathVariable("userIdx") int userIdx, @RequestBody PostReportStoryReq postReportStoryReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Stories
            GetStoriesRes getStoriesRes = storyProvider.getStories(postReportStoryReq.getStoryIdx());
            if (getStoriesRes.getGetStoriesByStoryIdxRes().get(0).getStoryImg() == null) {
                return new BaseResponse<>(FAILED_TO_STORIES);
            }
            // Report Stories
            storyService.reportStories(userIdx, postReportStoryReq.getStoryIdx(), postReportStoryReq.getContent());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 조회 유저 리스트 API
     * [GET] /stories/users/:userIdx/:storyIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @GetMapping("/users/{userIdx}/{storyIdx}")
    public BaseResponse<GetUserListRes> getUserList(@PathVariable("userIdx") int userIdx, @PathVariable("storyIdx") int storyIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Stories
            GetUserListRes getStoriesRes = storyProvider.getUserList(storyIdx);
            return new BaseResponse<>(getStoriesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 숨기기 / 취소 API
     * [PATCH] /stories/behinds/:userIdx/:followingIdx
     * @return BaseResponse<BaseResponseStatus>
     */
    @ResponseBody
    @PatchMapping("/behinds/{userIdx}/{followingIdx}")
    public BaseResponse<BaseResponseStatus> behindStory(@PathVariable("userIdx") int userIdx, @PathVariable("followingIdx") int followingIdx){
        try {

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int result = storyProvider.checkUserBehindStatus(userIdx, followingIdx);
            if (result == 0) {
                // Behind Story
                storyService.behindStory(userIdx, followingIdx);
            } else {
                // View Story
                storyService.viewStory(userIdx, followingIdx);
            }

            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
