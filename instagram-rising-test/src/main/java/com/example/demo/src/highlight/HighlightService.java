package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.src.highlight.model.PostHighlightReq;
import com.example.demo.src.story.StoryDao;
import com.example.demo.src.story.StoryProvider;
import com.example.demo.src.story.model.PostStoryReq;
import com.example.demo.src.story.model.PostStoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class HighlightService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HighlightDao highlightDao;

    private final HighlightProvider highlightProvider;

    private final JwtService jwtService;

    @Autowired
    public HighlightService(HighlightDao highlightDao, HighlightProvider highlightProvider, JwtService jwtService) {
        this.highlightDao = highlightDao;
        this.highlightProvider = highlightProvider;
        this.jwtService = jwtService;
    }

    public int createHighlights(int userIdx, int maxGroupIdx, PostHighlightReq postHighlightReq) throws BaseException {
        try {
            int groupIdx = highlightDao.createHighlights(userIdx, maxGroupIdx, postHighlightReq);
            return groupIdx;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
