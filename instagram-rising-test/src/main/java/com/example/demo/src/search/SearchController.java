package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.search.model.GetSearchListRes;
import com.example.demo.src.search.model.GetSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/searches/{userIdx}")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;

    @Autowired
    private final JwtService jwtService;


    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService) {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     * 유저 / 태그 / 장소 검색 API
     * [GET] /searches/:userIdx/:conditions
     * @param userIdx
     * @param word
     * @return BaseResponse<List<GetSearchLocaionsRes>>
     */
    @ResponseBody
    @GetMapping("/{conditions}")
    public BaseResponse<List<GetSearchRes>> searchByUsers(@PathVariable("userIdx") int userIdx , @PathVariable("conditions") String conditions , @RequestParam String word) {
        try {
            List<GetSearchRes> getSearchRes = new ArrayList<>();
            if (conditions.equals("users")) {
                // Get Users Results
                getSearchRes = searchProvider.searchByUsers(word);
            } else if (conditions.equals("locations")) {
                // Get Locations Results
                getSearchRes = searchProvider.searchByLocations(word);
            } else if (conditions.equals("tags")) {
                // Get Locations Results
                getSearchRes = searchProvider.searchByTags(word);
            } else {
                return new BaseResponse<>(SEARCH_EMPTY_CONDITION);
            }
            // Input Search Table
            searchService.createSearch(userIdx, word);
            return new BaseResponse<>(getSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 검색 기록 조회 API
     * [GET] /searches/:userIdx
     * @param userIdx
     * @return BaseResponse<List<GetSearchListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetSearchListRes>> getSearchList(@PathVariable("userIdx") int userIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetSearchListRes> getSearchRes = searchProvider.getSearchList(userIdx);
            return new BaseResponse<>(getSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 최근 검색 기록 삭제 API
     * [GET] /searches/:userIdx
     * @param userIdx
     * @return BaseResponse<List<GetSearchListRes>>
     */
    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<BaseResponseStatus> deleteSearch(@PathVariable("userIdx") int userIdx, @RequestParam(required = false) String word) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (word == null) {
                searchService.deleteSearchList(userIdx);
            } else {
                searchService.deleteSearchWord(userIdx, word);
            }
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
