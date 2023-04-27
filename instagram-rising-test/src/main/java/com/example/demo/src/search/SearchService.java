package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.FAILED_TO_WITHDRAW;

@Service
public class SearchService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    @Autowired
    public SearchService(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public void createSearch(int userIdx, String word) throws BaseException {
        try {
            searchDao.createSearch(userIdx, word);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteSearchList(int userIdx) throws BaseException {
        try {
            int result = searchDao.deleteSearchList(userIdx);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteSearchWord(int userIdx, String word) throws BaseException {
        try {
            int result = searchDao.deleteSearchWord(userIdx, word);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
