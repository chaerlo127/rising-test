package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.GetFollowsRes;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/{userIdx}")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;

    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService) {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    /**
     * 팔로우 하기 / 삭제 API
     * [post] /:userIdx/follows
     * @return BaseResponse<BaseResponseStatus>
     */
    @ResponseBody
    @PostMapping("/follows")
    public BaseResponse<BaseResponseStatus> createFollow(@PathVariable("userIdx") int userIdx, @RequestBody PostFollowReq postFollowReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postFollowReq.getFollowIdx() == null) {
                return new BaseResponse<>(POST_EMPTY_FOLLOW_ID);
            }
            int followingResult = followProvider.checkFollow(userIdx, postFollowReq.getFollowIdx());
            if (followingResult == 0 && postFollowReq.getStatus().equals("ACTIVE")) {
                // Create Follow
                followService.createFollow(userIdx, postFollowReq.getFollowIdx());
            } else if (followingResult != 0) {
                // Delete Follow or reCreate Follow
                followService.modifyFollow(userIdx, postFollowReq.getFollowIdx(), postFollowReq.getStatus());
            } else {
                return new BaseResponse<>(REQUEST_ERROR);
            }

            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로워 조회 API
     * [GET] /:userIdx/followers
     * @return BaseResponse<List<GetFollowersRes>>
     */
    @ResponseBody
    @GetMapping("/followers")
    public BaseResponse<List<GetFollowsRes>> getFollowers(@PathVariable("userIdx") int userIdx) {
        try{
            // Get Followers
            List<GetFollowsRes> GetFollowsRes = followProvider.getFollowers(userIdx);
            return new BaseResponse<>(GetFollowsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로잉 조회 API
     * [GET] /:userIdx/followings
     * @return BaseResponse<List<GetFollowersRes>>
     */
    @ResponseBody
    @GetMapping("/followings")
    public BaseResponse<List<GetFollowsRes>> getFollowings(@PathVariable("userIdx") int userIdx) {
        try{
            // Get Followers
            List<GetFollowsRes> GetFollowsRes = followProvider.getFollowings(userIdx);
            return new BaseResponse<>(GetFollowsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 추천친구 조회 API
     * [GET] /:userIdx/recommends
     * @return BaseResponse<List<GetFollowersRes>>
     */
    @ResponseBody
    @GetMapping("/recommends")
    public BaseResponse<List<GetFollowsRes>> getRecommends(@PathVariable("userIdx") int userIdx) {
        try{
            // 추천친구 기준
            // 1. 상대는 나를 팔로우 했지만 나는 팔로우 안한 사람
            // 2. 내가 팔로우 한 사람들 중 2명 이상이 팔로우 한 사람
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Recommends
            List<GetFollowsRes> GetFollowsRes = followProvider.getRecommends(userIdx);
            return new BaseResponse<>(GetFollowsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 함께 아는 친구 조회 API
     * [GET] /:userIdx/followers/together/:followIdx
     * @return BaseResponse<List<GetFollowersRes>>
     */
    @ResponseBody
    @GetMapping("/followers/together/{followIdx}")
    public BaseResponse<List<GetFollowsRes>> getTogether(@PathVariable("userIdx") int userIdx, @PathVariable("followIdx") int followIdx) {
        try{
            // 함께 아는 친구 기준
            // 내가 팔로우 한 사람을 내 팔로워도 팔로우 한 경우
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // Get Recommends
            List<GetFollowsRes> GetFollowsRes = followProvider.getTogether(userIdx, followIdx);
            return new BaseResponse<>(GetFollowsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
