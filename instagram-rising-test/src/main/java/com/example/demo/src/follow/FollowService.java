package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final JwtService jwtService;

    public FollowService(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public void createFollow(int userIdx, String followIdx) throws BaseException {
        try {
            int result = followDao.createFollow(userIdx, followIdx);
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    public void modifyFollow(int userIdx, String followIdx, String status) throws BaseException {
        try {
            int result = followDao.modifyFollow(userIdx, followIdx, status);
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
