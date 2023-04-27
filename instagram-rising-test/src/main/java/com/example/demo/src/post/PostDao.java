package com.example.demo.src.post;

import com.example.demo.src.comment.model.GetCommentLikeRes;
import com.example.demo.src.post.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;
    private List<GetPostImgRes> getPostImgRes;
    private List<GetPostConmmentRes> getPostConmmentRes;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createPost(int userIdx, PostReq postPostReq) {
        String createPostQuery = "insert into Post (content, userIdx) VALUES (?,?)";
        Object[] createPostParams = new Object[]{postPostReq.getContent(), userIdx};
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public void saveAll(PostReq postPostReq, int postIdx) {
        int batchCount = 0;
        List<PostImgReq> subItems = new ArrayList<>();
        for (int i = 0; i < postPostReq.getPostImgReqs().size(); i++) {
            subItems.add(postPostReq.getPostImgReqs().get(i));
            batchCount = createPostImg(batchCount, subItems, postIdx);
        }
        if (!subItems.isEmpty()) {
            batchCount = createPostImg(batchCount, subItems, postIdx);
        }
        System.out.println("batchCount: " + batchCount);
    }

    public int createPostImg(int batchCount, List<PostImgReq> postPostReq, int postIdx) {
        jdbcTemplate.batchUpdate("insert into PostImg (postIdx, postImgUrl) VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, postIdx);
                ps.setString(2, postPostReq.get(i).getPostImgUrl());
            }
            @Override
            public int getBatchSize() {
                return postPostReq.size();
            }
        });
        postPostReq.clear();
        batchCount++;
        return batchCount;
    }

    public GetPostRes getPostByPostIdx(int postIdx) {
        try{
            String getPostQuery = "select Post.postIdx, Post.content, U.userIdx, U.userId, U.profileImg,\n" +
                    "IF(postLikeCount is null, 0, postLikeCount) as postLike,\n" +
                    "case when timestampdiff(second, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(second, Post.updateAt, current_timestamp), '초 전')\n" +
                    "when timestampdiff(minute, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(minute, Post.updateAt, current_timestamp), '분 전')\n" +
                    "when timestampdiff(hour, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(hour, Post.updateAt, current_timestamp), '시간 전')\n" +
                    "when timestampdiff(day, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(day, Post.updateAt, current_timestamp), '일 전')\n" +
                    "else timestampdiff(year, Post.updateAt, current_timestamp) end as updatedAt\n" +
                    "from Post\n" +
                    "inner join User U on Post.userIdx = U.userIdx\n" +
                    "left join PostImg PI on Post.postIdx = PI.postIdx\n" +
                    "left join (select postIdx, count(userIdx) as postLikeCount from postLike WHERE status = 'ACTIVE' group by postIdx) PL on Post.postIdx = PL.postIdx\n" +
                    "where Post.postIdx = ? and Post.status = 'ACTIVE'\n" +
                    "group by Post.postIdx";
            int getPostsByIdxParams = postIdx;
            return this.jdbcTemplate.queryForObject(getPostQuery,
                    (rs,rowNum) -> new GetPostRes(
                            rs.getInt("postIdx"),
                            rs.getString("content"),
                            rs.getInt("userIdx"),
                            rs.getString("userId"),
                            rs.getString("profileImg"),
                            rs.getInt("postLike"),
                            rs.getString("updatedAt"),
                            getPostImgRes = this.jdbcTemplate.query("select pi.postImgIdx, pi.postImgUrl\n" +
                                            "from PostImg as pi\n" +
                                            "join Post p on pi.postIdx = p.postIdx\n" +
                                            "where pi.status = 'ACTIVE' and p.postIdx = ?",
                                    (rk, rownum) -> new GetPostImgRes(
                                            rk.getInt("postImgIdx"),
                                            rk.getString("postImgUrl")
                                    ), postIdx),
                            getPostConmmentRes = this.jdbcTemplate.query("select PC.commentIdx, PC.reply, PC.depth, PC.userIdx, U.userId, U.profileImg, PC.commentAIdx, PC.postIdx,\n" +
                                            "IF(commentLikeCount is null, 0, commentLikeCount) as commentLike,\n" +
                                            "case when timestampdiff(second, PC.updateAt, current_timestamp) < 60 then concat(timestampdiff(second, PC.updateAt, current_timestamp), '초 전')\n" +
                                            "when timestampdiff(minute, PC.updateAt, current_timestamp) < 60 then concat(timestampdiff(minute, PC.updateAt, current_timestamp), '분 전')\n" +
                                            "when timestampdiff(hour, PC.updateAt, current_timestamp) < 60 then concat(timestampdiff(hour, PC.updateAt, current_timestamp), '시간 전')\n" +
                                            "when timestampdiff(day, PC.updateAt, current_timestamp) < 60 then concat(timestampdiff(day, PC.updateAt, current_timestamp), '일 전')\n" +
                                            "else timestampdiff(year, PC.updateAt, current_timestamp) end as updatedAt\n" +
                                            "from Comment as PC\n" +
                                            "left join User U on PC.userIdx = U.userIdx\n" +
                                            "left join Post p on PC.postIdx = p.postIdx\n" +
                                            "left join (select commentIdx, count(userIdx) as commentLikeCount from insta.CommentLike WHERE status = 'ACTIVE' group by commentIdx) CL on PC.commentIdx = CL.commentIdx\n" +
                                            "where p.postIdx = ?;",
                                    (rk, rownum) -> new GetPostConmmentRes(
                                            rk.getInt("commentIdx"),
                                            rk.getString("reply"),
                                            rk.getInt("depth"),
                                            rk.getInt("userIdx"),
                                            rk.getInt("commentLike"),
                                            rk.getString("updatedAt"),
                                            rk.getString("userId"),
                                            rk.getString("profileImg"),
                                            rk.getInt("commentAIdx")
                                    ), postIdx)
                    ),
                    getPostsByIdxParams);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int updatePostsByPostIdxAndUserIdx(int postIdx, int userIdx, PatchReq patchReq) {
        String modifyPostContentQuery = "update Post set content = ? where userIdx = ? AND postIdx = ?";
        Object[] modifyPostContentParams = new Object[]{patchReq.getContent(), userIdx, postIdx};

        return this.jdbcTemplate.update(modifyPostContentQuery, modifyPostContentParams);
    }

    public int deletePostByPostIdxAndUserIdx(int postIdx, int userIdx) {
        String deletePostQuery = "delete from Post where userIdx = ? and postIdx = ?";
        Object[] deletePostParams = new Object[]{userIdx, postIdx};
        return this.jdbcTemplate.update(deletePostQuery, deletePostParams);
    }

    public int deletePostLikeByPostIdxAndUserIdx(int postIdx) {
        String deletePostLikeQuery = "delete from postLike where postIdx = ?";
        Object[] deletePostLikeParams = new Object[]{postIdx};
        return this.jdbcTemplate.update(deletePostLikeQuery, deletePostLikeParams);
    }

    public int deletePostCommentByPostIdxAndUserIdx(int postIdx) {
        String deletePostCommentQuery = "delete from Comment where postIdx = ?";
        Object[] deletePostCommentParams = new Object[]{postIdx};
        return this.jdbcTemplate.update(deletePostCommentQuery, deletePostCommentParams);
    }

    public int deletePostImgByPostIdxAndUserIdx(int postIdx) {
        String deletePostImgQuery = "delete from PostImg where postIdx = ?";
        Object[] deletePostImgParams = new Object[]{postIdx};
        return this.jdbcTemplate.update(deletePostImgQuery, deletePostImgParams);
    }

    public List<GetFeedRes> getFeed() {
        try{
            String getPostQuery = "select Post.postIdx, Post.content, U.userIdx, U.userId, U.profileImg,\n" +
                    "IF(postLikeCount is null, 0, postLikeCount) as postLike,\n" +
                    "IF(commentCount is null, 0, commentCount) as commentCount,\n" +
                    "case when timestampdiff(second, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(second, Post.updateAt, current_timestamp), '초 전')\n" +
                    "when timestampdiff(minute, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(minute, Post.updateAt, current_timestamp), '분 전')\n" +
                    "when timestampdiff(hour, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(hour, Post.updateAt, current_timestamp), '시간 전')\n" +
                    "when timestampdiff(day, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(day, Post.updateAt, current_timestamp), '일 전')\n" +
                    "else timestampdiff(year, Post.updateAt, current_timestamp) end as updatedAt\n" +
                    "from Post\n" +
                    "inner join User U on Post.userIdx = U.userIdx\n" +
                    "left join PostImg PI on Post.postIdx = PI.postIdx\n" +
                    "left join (select postIdx, count(userIdx) as postLikeCount from postLike WHERE status = 'ACTIVE' group by postIdx) PL on Post.postIdx = PL.postIdx\n" +
                    "left join (select postIdx, count(commentIdx) as commentCount from Comment WHERE status = 'ACTIVE' group by postIdx) PC on Post.postIdx = PC.postIdx\n" +
                    "group by Post.postIdx \n" +
                    "order by Post.updateAt desc";
            return this.jdbcTemplate.query(getPostQuery,
                    (rs,rowNum) -> new GetFeedRes(
                            rs.getInt("postIdx"),
                            rs.getString("content"),
                            rs.getInt("userIdx"),
                            rs.getString("userId"),
                            rs.getString("profileImg"),
                            rs.getInt("postLike"),
                            rs.getInt("commentCount"),
                            rs.getString("updatedAt"),
                            getPostImgRes = this.jdbcTemplate.query("select pi.postImgIdx, pi.postImgUrl\n" +
                                            "from PostImg as pi\n" +
                                            "join Post p on pi.postIdx = p.postIdx\n" +
                                            "where pi.status = 'ACTIVE' and p.postIdx = ?",
                                    (rk, rownum) -> new GetPostImgRes(
                                            rk.getInt("postImgIdx"),
                                            rk.getString("postImgUrl")
                                    ), rs.getInt("postIdx"))
                    ));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<GetPostLikeListRes> getPostLikeList(int postIdx, int userIdx) {
        String getPostLikeListQuery = "select U.userIdx, U.name, U.userId, U.profileImg,\n" +
                "    IF(followerIdx is null, 'INACTIVE', 'ACTIVE') as followStatus\n" +
                "from postLike\n" +
                "left join User U on postLike.userIdx = U.userIdx\n" +
                "left join (select followerIdx  from Follow WHERE status = 'ACTIVE' and followingIdx = ?) F on F.followerIdx = U.userIdx\n" +
                "where postLike.status = 'ACTIVE'\n" +
                "and postLike.postIdx = ?";
        Object[] getPostLikeListParams = new Object[]{userIdx, postIdx};
        return this.jdbcTemplate.query(getPostLikeListQuery,
                (rs,rowNum) -> new GetPostLikeListRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("userId"),
                        rs.getString("profileImg"),
                        rs.getString("followStatus")),
                getPostLikeListParams
        );
    }

    public List<GetRandFeedListRes> getRanFeedList() {
        String getRanFeedListQuery = "select Post.postIdx, PI.postImgUrl\n" +
                "from Post\n" +
                "left outer join PostImg PI on Post.postIdx = PI.postIdx\n" +
                "group by Post.postIdx\n" +
                "order by rand()";
        return this.jdbcTemplate.query(getRanFeedListQuery,
                (rs,rowNum) -> new GetRandFeedListRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl"))
        );
    }

    public List<GetRandFeedRes> getFeedRand(int postIdx) {
        try{
            String getPostQuery = "select Post.postIdx, Post.content, U.userIdx, U.userId, U.profileImg,\n" +
                    "IF(postLikeCount is null, 0, postLikeCount) as postLike,\n" +
                    "IF(commentCount is null, 0, commentCount) as commentCount,\n" +
                    "case when timestampdiff(second, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(second, Post.updateAt, current_timestamp), '초 전')\n" +
                    "when timestampdiff(minute, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(minute, Post.updateAt, current_timestamp), '분 전')\n" +
                    "when timestampdiff(hour, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(hour, Post.updateAt, current_timestamp), '시간 전')\n" +
                    "when timestampdiff(day, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(day, Post.updateAt, current_timestamp), '일 전')\n" +
                    "else timestampdiff(year, Post.updateAt, current_timestamp) end as updatedAt\n" +
                    "from Post\n" +
                    "inner join User U on Post.userIdx = U.userIdx\n" +
                    "left join PostImg PI on Post.postIdx = PI.postIdx\n" +
                    "left join (select postIdx, count(userIdx) as postLikeCount from postLike WHERE status = 'ACTIVE' group by postIdx) PL on Post.postIdx = PL.postIdx\n" +
                    "left join (select postIdx, count(commentIdx) as commentCount from Comment WHERE status = 'ACTIVE' group by postIdx) PC on Post.postIdx = PC.postIdx\n" +
                    "where Post.postIdx = ?\n" +
                    "group by Post.postIdx";
            return getGetRandFeedRes(postIdx, getPostQuery);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @NotNull
    private List<GetRandFeedRes> getGetRandFeedRes(int postIdx, String getPostQuery) {
        return this.jdbcTemplate.query(getPostQuery,
                (rs,rowNum) -> new GetRandFeedRes(
                        rs.getInt("postIdx"),
                        rs.getString("content"),
                        rs.getInt("userIdx"),
                        rs.getString("userId"),
                        rs.getString("profileImg"),
                        rs.getInt("postLike"),
                        rs.getInt("commentCount"),
                        rs.getString("updatedAt"),
                        getPostImgRes = this.jdbcTemplate.query("select pi.postImgIdx, pi.postImgUrl\n" +
                                        "from PostImg as pi\n" +
                                        "join Post p on pi.postIdx = p.postIdx\n" +
                                        "where pi.status = 'ACTIVE' and p.postIdx = ?",
                                (rk, rownum) -> new GetPostImgRes(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("postImgUrl")
                                ), rs.getInt("postIdx"))
                ), postIdx);
    }

    public List<GetRandFeedRes> getPostForRand(int postIdx) {
        try{
            String getPostQuery = "select Post.postIdx, Post.content, U.userIdx, U.userId, U.profileImg,\n" +
                    "IF(postLikeCount is null, 0, postLikeCount) as postLike,\n" +
                    "IF(commentCount is null, 0, commentCount) as commentCount,\n" +
                    "case when timestampdiff(second, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(second, Post.updateAt, current_timestamp), '초 전')\n" +
                    "when timestampdiff(minute, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(minute, Post.updateAt, current_timestamp), '분 전')\n" +
                    "when timestampdiff(hour, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(hour, Post.updateAt, current_timestamp), '시간 전')\n" +
                    "when timestampdiff(day, Post.updateAt, current_timestamp) < 60 then concat(timestampdiff(day, Post.updateAt, current_timestamp), '일 전')\n" +
                    "else timestampdiff(year, Post.updateAt, current_timestamp) end as updatedAt\n" +
                    "from Post\n" +
                    "inner join User U on Post.userIdx = U.userIdx\n" +
                    "left join PostImg PI on Post.postIdx = PI.postIdx\n" +
                    "left join (select postIdx, count(userIdx) as postLikeCount from postLike WHERE status = 'ACTIVE' group by postIdx) PL on Post.postIdx = PL.postIdx\n" +
                    "left join (select postIdx, count(commentIdx) as commentCount from Comment WHERE status = 'ACTIVE' group by postIdx) PC on Post.postIdx = PC.postIdx\n" +
                    "where Post.postIdx != ?\n" +
                    "group by Post.postIdx\n" +
                    "order by rand()";
            return getGetRandFeedRes(postIdx, getPostQuery);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public GetPostLikeRes getPostLikeByPostIdxAndUserIdx(int postIdx, int userIdx) {
        try {
            String getUserQuery = "select * from postLike where postIdx = ? and userIdx = ?";
            Object[] modifyPostLikeParams = new Object[]{postIdx, userIdx};
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetPostLikeRes(
                            rs.getInt("postLikeIdx"),
                            rs.getInt("postIdx"),
                            rs.getInt("userIdx"),
                            rs.getString("status")),
                    modifyPostLikeParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int createPostLike(int postIdx, int userIdx) {
        String createPostLikeQuery = "insert into postLike (postIdx, userIdx) VALUES (?,?)";
        Object[] createPostLikeParams = new Object[]{postIdx, userIdx};
        this.jdbcTemplate.update(createPostLikeQuery, createPostLikeParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int patchPostLike(int postIdx, int userIdx, String status) {
        String modifyPostContentQuery = "update postLike set status = ? where userIdx = ? AND postIdx = ?";
        Object[] modifyPostContentParams = new Object[]{status, userIdx, postIdx};

        return this.jdbcTemplate.update(modifyPostContentQuery, modifyPostContentParams);
    }

    public GetPostReportRes getPostReportByUserIdxPostIdx(int postIdx, int userIdx) {
        try {
            String getUserQuery = "select * from PostReport where postIdx = ? and userIdx = ?";
            Object[] modifyPostReportParams = new Object[]{postIdx, userIdx};
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetPostReportRes(
                            rs.getInt("postReportIdx"),
                            rs.getInt("postIdx"),
                            rs.getInt("userIdx"),
                            rs.getString("status")),
                    modifyPostReportParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int createPostReport(int postIdx, int userIdx, PostReportReq postReportReq) {
        String createPostReportQuery = "insert into PostReport (postIdx, userIdx, reason) VALUES (?,?,?)";
        Object[] createPostReportParams = new Object[]{postIdx, userIdx, postReportReq.getReason()};
        this.jdbcTemplate.update(createPostReportQuery, createPostReportParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    public String userLikeFeedListRecent(){
        return  "select Post.postIdx, PI.postImgUrl\n" +
                "from Post\n" +
                "left outer join PostImg PI on Post.postIdx = PI.postIdx\n" +
                "right outer join postLike pL on Post.postIdx = pL.postIdx\n" +
                "where pL.userIdx = ?\n" +
                "group by Post.postIdx, pL.userIdx\n" +
                "order by Post.createAt desc";
    }

    public String userLikeFeedListPast(){
        return  "select Post.postIdx, PI.postImgUrl\n" +
                "from Post\n" +
                "left outer join PostImg PI on Post.postIdx = PI.postIdx\n" +
                "right outer join postLike pL on Post.postIdx = pL.postIdx\n" +
                "where pL.userIdx = ?\n" +
                "group by Post.postIdx, pL.userIdx\n" +
                "order by Post.createAt";
    }

    public List<GetUserLikeFeedsListRes> getUserLikeFeedsList(int userIdx, String sql) {
        String getuserLikeFeedListQuery = sql;
        return this.jdbcTemplate.query(getuserLikeFeedListQuery,
                (rs,rowNum) -> new GetUserLikeFeedsListRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")),
                userIdx
        );
    }

    public GetPostSaveRes getPostSaveByPostIdxAndUserIdx(int postIdx, int userIdx) {
        try {
            String getUserQuery = "select * from PostsSave where postIdx = ? and userIdx = ?";
            Object[] modifyPostReportParams = new Object[]{postIdx, userIdx};
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetPostSaveRes(
                            rs.getInt("postSaveIdx"),
                            rs.getInt("postIdx"),
                            rs.getInt("userIdx"),
                            rs.getString("status")),
                    modifyPostReportParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int createPostSave(int postIdx, int userIdx) {
        String createPostReportQuery = "insert into PostsSave (postIdx, userIdx) VALUES (?,?)";
        Object[] createPostReportParams = new Object[]{postIdx, userIdx};
        this.jdbcTemplate.update(createPostReportQuery, createPostReportParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int patchPostSave(int postIdx, int userIdx, String status) {
        String modifyPostSaveQuery = "update PostsSave set status = ? where userIdx = ? AND postIdx = ?";
        Object[] modifyPostSaveParams = new Object[]{status, userIdx, postIdx};

        return this.jdbcTemplate.update(modifyPostSaveQuery, modifyPostSaveParams);
    }

    public List<GetUserSaveFeedsListRes> getUserSavesFeedsList(int userIdx) {
        String getUserSavesFeedListQuery = "select Post.postIdx, PI.postImgUrl\n" +
                "from Post\n" +
                "left outer join PostImg PI on Post.postIdx = PI.postIdx\n" +
                "right outer join PostsSave s on Post.postIdx = s.postIdx\n" +
                "where s.userIdx = ?\n" +
                "group by Post.postIdx, s.userIdx\n" +
                "order by Post.createAt desc";
        return this.jdbcTemplate.query(getUserSavesFeedListQuery,
                (rs,rowNum) -> new GetUserSaveFeedsListRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")),
                userIdx
        );
    }
}
