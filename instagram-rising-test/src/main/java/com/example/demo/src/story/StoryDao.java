package com.example.demo.src.story;

import com.example.demo.src.post.model.GetPostImgRes;
import com.example.demo.src.story.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class StoryDao {
    private JdbcTemplate jdbcTemplate;
    private List<GetTagUsersRes> getTagUsersRes;
    private List<GetStoriesByStoryIdxRes> getStoriesByStoryIdxRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetStoriesRes getStories(int storyIdx) {
        String getStoriesQuery = "select si.storyImgUrl, u.profileImg, u.userId, DATE_FORMAT(TIMEDIFF(now(),s.updateAt),'%H') as postHourAgo " +
                ",s.content, max(l.name) as tagLocation " +
                "from Story s " +
                "inner join User u on s.userIdx = u.userIdx " +
                "inner join StoryImg si on s.storyIdx = si.storyIdx " +
                "left join StoryTag st on st.storyIdx = s.storyIdx " +
                "left join Location l on l.locationIdx = st.locationIdx " +
                "where s.storyIdx = ? and s.status = 'ACTIVE'";
        int getStoriesParam = storyIdx;
        getStoriesByStoryIdxRes = this.jdbcTemplate.query(getStoriesQuery,
                (rs,rowNum) -> new GetStoriesByStoryIdxRes(
                        rs.getString("storyImgUrl"),
                        rs.getString("profileImg"),
                        rs.getString("userId"),
                        rs.getString("postHourAgo"),
                        rs.getString("content"),
                        rs.getString("tagLocation"),
                        getTagUsersRes = this.jdbcTemplate.query("select u.userIdx, u.userId" +
                                                                " from User u inner join StoryTag st on u.userIdx = st.userIdx" +
                                                                " inner join Story s on st.storyIdx = s.storyIdx" +
                                                                " where s.storyIdx = ?" +
                                                                " and s.status = 'ACTIVE'",
                                                        (rk, rownum) -> new GetTagUsersRes(
                                                                rk.getInt("userIdx"),
                                                                rk.getString("userId")
                                                        ), getStoriesParam)
                        ), getStoriesParam
        );
        return new GetStoriesRes(storyIdx, getStoriesByStoryIdxRes);
    }

    public List<GetStoriesByUserRes> getStoriesByUser(int userIdx){
        String getStoriesByUserQuery = "select s.userIdx, si.storyImgUrl as img, s.updateAt as time " +
                "from Story s " +
                "inner join StoryImg si on s.storyIdx = si.storyIdx " +
                "where userIdx = ?";
        int getStoriesByUserParams = userIdx;
        return this.jdbcTemplate.query(getStoriesByUserQuery,
                (rs, rowNum) -> new GetStoriesByUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("img"),
                        rs.getString("time")),
                getStoriesByUserParams);
    }

    public int createStoryIdx(int userIdx, PostStoryReq postStoryReq){
        String createStoryQuery = "insert into Story (userIdx, content) VALUES (?,?)";
        Object[] createStoryParams = new Object[]{userIdx, postStoryReq.getContent()};
        this.jdbcTemplate.update(createStoryQuery, createStoryParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createStoryByStoryIdx(PostStoryReq postStoryReq, int storyIdx) {

        String createStoryImgQuery = "insert into StoryImg (storyImgUrl, storyIdx) VALUES (?,?)";
        Object[] createStoryImgParams = new Object[]{postStoryReq.getImg(), storyIdx};
        this.jdbcTemplate.update(createStoryImgQuery, createStoryImgParams);

        if (postStoryReq.getTagLocationIdx() != null) {
            if (postStoryReq.getTagUserIdx() == null || postStoryReq.getTagUserIdx().isEmpty()) {
                String createStoryTagQuery = "insert into StoryTag (storyIdx, locationIdx) VALUES (?,?)";
                Object[] createStoryTagParams = new Object[]{storyIdx, postStoryReq.getTagLocationIdx()};
                this.jdbcTemplate.update(createStoryTagQuery, createStoryTagParams);
            } else {
                for (int tagUserIdx : postStoryReq.getTagUserIdx()) {
                    String createStoryTagQuery = "insert into StoryTag (userIdx, storyIdx, locationIdx) VALUES (?,?,?)";
                    Object[] createStoryTagParams = new Object[]{tagUserIdx, storyIdx, postStoryReq.getTagLocationIdx()};
                    this.jdbcTemplate.update(createStoryTagQuery, createStoryTagParams);
                }
            }
        }
    }

    public void deleteStoryByUser(int storyIdx){
        String deleteStoryQuery = "delete from Story where storyIdx=?";
        int deleteStoryParams = storyIdx;
        this.jdbcTemplate.update(deleteStoryQuery, deleteStoryParams);

        String deleteStoryTagQuery = "delete from StoryTag where storyIdx=?";
        int deleteStoryTagParams = storyIdx;
        this.jdbcTemplate.update(deleteStoryTagQuery, deleteStoryTagParams);

        String deleteStoryImgQuery = "delete from StoryImg where storyIdx=?";
        int deleteStoryImgParams = storyIdx;
        this.jdbcTemplate.update(deleteStoryImgQuery, deleteStoryImgParams);

    }
    public void deleteStory(){
        String deleteStoryQuery = "update Story set status = 'INACTIVE' where DATE_FORMAT(TIMEDIFF(now(),updateAt),'%H') > 24";
        this.jdbcTemplate.update(deleteStoryQuery);

        String deleteStoryTagQuery = "update StoryTag set status = 'INACTIVE' where DATE_FORMAT(TIMEDIFF(now(),updateAt),'%H') > 24";
        this.jdbcTemplate.update(deleteStoryTagQuery);

        String deleteStoryImgQuery = "update StoryImg set status = 'INACTIVE' where DATE_FORMAT(TIMEDIFF(now(),updateAt),'%H') > 24";
        this.jdbcTemplate.update(deleteStoryImgQuery);

    }

    public void likeStories(int userIdx, int storyIdx){
        String createStoryQuery = "insert into StoryLike (userIdx, storyIdx) VALUES (?,?)";
        Object[] createStoryParams = new Object[]{userIdx, storyIdx};
        this.jdbcTemplate.update(createStoryQuery, createStoryParams);
    }

    public void dislikeStories(int userIdx, int storyIdx, String status){
        String createStoryQuery = "update StoryLike set status = ? where userIdx = ? and storyIdx = ?";
        Object[] createStoryParams = new Object[]{status, userIdx, storyIdx};
        this.jdbcTemplate.update(createStoryQuery, createStoryParams);
    }

    public int checkLike(int userIdx, int storyIdx) {
        String checkLikeQuery = "select exists(select storyLikeIdx from StoryLike where userIdx = ? and storyIdx = ?)";
        Object[] checkLikeParams = new Object[]{userIdx, storyIdx};
        return this.jdbcTemplate.queryForObject(checkLikeQuery,
                int.class,
                checkLikeParams);
    }

    public void reportStories(int userIdx, int storyIdx, String content){
        String reportStoryQuery = "insert into StoryReport (StoryIdx, userIdx, reason) values (?,?, ?)";
        Object[] reportStoriesParams = new Object[]{storyIdx, userIdx, content};
        this.jdbcTemplate.update(reportStoryQuery, reportStoriesParams);

    }

    public void createViewer(int userIdx, int storyIdx){
        String createViewerQuery = "insert into StoryViewer (StoryIdx, userIdx) values (?,?)";
        Object[] createViewerParams = new Object[]{storyIdx, userIdx};
        this.jdbcTemplate.update(createViewerQuery, createViewerParams);

    }

    public GetUserListRes getUserList(int storyIdx) {
        String getUserQuery = "select u.userIdx, u.userId, u.name, u.profileImg as img, sl.status as likeYn, count(*) as cnt " +
                "from User u inner join StoryViewer sv on u.userIdx = sv.userIdx " +
                "left join StoryLike sl on sv.userIdx = sl.userIdx " +
                "where sl.storyIdx = ?";
        int getUserParams = storyIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserListRes(
                        rs.getInt("userIdx"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("img"),
                        rs.getString("likeYn"),
                        rs.getInt("cnt")),
                getUserParams);
    }

    public void behindStory(int userIdx, int followingIdx){
        String behindStoryQuery = "insert into Behind (userIdx, followingIdx, story) values (?,?,1)";
        Object[] behindStoryParams = new Object[]{userIdx, followingIdx};
        this.jdbcTemplate.update(behindStoryQuery, behindStoryParams);
    }

    public void viewStory(int userIdx, int followingIdx){
        String behindStoryQuery = "update Behind set story = null where userIdx = ? and followingIdx = ?";
        Object[] behindStoryParams = new Object[]{userIdx, followingIdx};
        this.jdbcTemplate.update(behindStoryQuery, behindStoryParams);
    }

    public int checkUserBehindStatus(int userIdx, int followingIdx) {
        String checkUserBehindStatusQuery = "select exists(select behindIdx from Behind where userIdx = ? and followingIdx = ? and story = 1)";
        Object[] checkUserBehindStatusParams = new Object[]{userIdx, followingIdx};
        return this.jdbcTemplate.queryForObject(checkUserBehindStatusQuery,
                int.class,
                checkUserBehindStatusParams);
    }
}
