package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.src.highlight.model.GetHighlightsRes;
import com.example.demo.src.story.model.GetStoriesByUserRes;
import com.example.demo.src.story.model.GetUserListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class HighlightProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HighlightDao highlightDao;

    private final JwtService jwtService;

    @Autowired
    public HighlightProvider( HighlightDao highlightDao, JwtService jwtService) {
        this.highlightDao = highlightDao;
        this.jwtService = jwtService;
    }

    public int maxIdx() throws BaseException {
        try {
            int maxIdx = highlightDao.maxIdx();
            return maxIdx;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHighlightsRes> getHighlight(int userIdx) throws BaseException {
        try {
            List<GetHighlightsRes> getHighlightsRes = highlightDao.getHighlight(userIdx);
            return getHighlightsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
