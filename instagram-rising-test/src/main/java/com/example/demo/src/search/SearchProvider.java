package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.GetSearchListRes;
import com.example.demo.src.search.model.GetSearchRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SearchProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    @Autowired
    public SearchProvider(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public List<GetSearchRes> searchByUsers(String word) throws BaseException {
        try{
            List<GetSearchRes> getSearchRes = searchDao.searchByUsers(word);
            return getSearchRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchRes> searchByLocations(String word) throws BaseException {
        try{
            List<GetSearchRes> getSearchLocaionsRes = searchDao.searchByLocations(word);
            return getSearchLocaionsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchRes> searchByTags(String word) throws BaseException {
        try{
            List<GetSearchRes> getSearchTagsRes = searchDao.searchByTags(word);
            return getSearchTagsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchListRes> getSearchList(int userIdx) throws BaseException {
        try{
            List<GetSearchListRes> getSearchTagsRes = searchDao.getSearchList(userIdx);
            return getSearchTagsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
