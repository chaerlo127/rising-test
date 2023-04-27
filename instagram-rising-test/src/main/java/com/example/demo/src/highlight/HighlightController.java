package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.highlight.model.GetHighlightsRes;
import com.example.demo.src.highlight.model.PostHighlightReq;
import com.example.demo.src.story.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/highlights")
public class HighlightController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HighlightService highlightService;

    @Autowired
    private final HighlightProvider highlightProvider;

    @Autowired
    private final JwtService jwtService;


    public HighlightController(JwtService jwtService, HighlightService highlightService, HighlightProvider highlightProvider) {
        this.jwtService = jwtService;
        this.highlightService = highlightService;
        this.highlightProvider = highlightProvider;
    }

    /**
     * 하이라이트 전체 조회 API
     * [GET] /highlights/:userIdx
     * @return BaseResponse<List<GetHighlightsRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetHighlightsRes>> getStories(@PathVariable("userIdx") int userIdx) {
        try {
            // Get Highlight
            List<GetHighlightsRes> getHighlightsRes = highlightProvider.getHighlight(userIdx);
            return new BaseResponse<>(getHighlightsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 하이라이트 추가 API
     * [POST] /highlights/:userIdx
     * @return BaseResponse<List<GetStoriesRes>>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<Integer> createHighlights(@PathVariable("userIdx") int userIdx, @RequestBody PostHighlightReq postHighlightReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int maxGroupIdx = highlightProvider.maxIdx() + 1;
            int groupIdx = highlightService.createHighlights(userIdx, maxGroupIdx, postHighlightReq);

            return new BaseResponse<>(groupIdx);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
