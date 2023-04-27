package com.example.demo.src.comment;

import com.example.demo.src.comment.model.*;
import com.example.demo.src.post.model.GetPostReportRes;
import com.example.demo.src.post.model.PostReportReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createComment(int postIdx, int userIdx, PostCommentReq postCommentReq) {
        String createCommentQuery = "insert into Comment (userIdx, postIdx, reply, depth, commentAIdx) VALUES (?,?,?,?,?)";
        Object[] createcommentParams = new Object[]{userIdx, postIdx, postCommentReq.getReply(), postCommentReq.getDepth(), postCommentReq.getCommentAIdx()};
        this.jdbcTemplate.update(createCommentQuery, createcommentParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public GetCommentRes getCommentByCommentIdx(int commentIdx) {
        try {
            String getUserQuery = "select * from Comment where commentIdx = ? AND status = 'ACTIVE'";
            int getUserParams = commentIdx;
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetCommentRes(
                            rs.getInt("commentIdx"),
                            rs.getString("reply"),
                            rs.getInt("commentAIdx"),
                            rs.getInt("depth"),
                            rs.getInt("userIdx"),
                            rs.getInt("postIdx")),
            getUserParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int deleteComment(int commentIdx, int userIdx) {
        String modifyCommentReplyQuery = "update Comment set reply = '삭제된 댓글 입니다.', status = 'INACTIVE' where commentIdx = ? AND userIdx = ?";
        Object[] modifyCommentReplyParams = new Object[]{commentIdx, userIdx};

        return this.jdbcTemplate.update(modifyCommentReplyQuery, modifyCommentReplyParams);
    }

    public GetCommentLikeRes getCommentLikeByCommentIdxAndUserIdx(int commentIdx, int userIdx) {
        try {
            String getUserQuery = "select * from CommentLike where commentIdx = ? and userIdx = ?";
            Object[] modifyCommentLikeParams = new Object[]{commentIdx, userIdx};
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetCommentLikeRes(
                            rs.getInt("commentLikeIdx"),
                            rs.getInt("commentIdx"),
                            rs.getInt("userIdx"),
                            rs.getString("status")),
                    modifyCommentLikeParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int createCommentLike(int commentIdx, int userIdx) {
        String createCommentQuery = "insert into CommentLike (commentIdx, userIdx) VALUES (?,?)";
        Object[] createcommentParams = new Object[]{commentIdx, userIdx};
        this.jdbcTemplate.update(createCommentQuery, createcommentParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int patchCommentLike(int commentIdx, int userIdx, String status) {
        String modifyPostContentQuery = "update CommentLike set status = ? where userIdx = ? AND commentIdx = ?";
        Object[] modifyPostContentParams = new Object[]{status, userIdx, commentIdx};

        return this.jdbcTemplate.update(modifyPostContentQuery, modifyPostContentParams);
    }

    public List<GetCommentLikeListRes> getCommentLikeList(int commentIdx, int userIdx) {
        String getCommentLikeListQuery = "select U.userIdx, U.name, U.userId, U.profileImg,\n" +
                "    IF(followerIdx is null, 'INACTIVE', 'ACTIVE') as followStatus\n" +
                "from CommentLike\n" +
                "left join User U on CommentLike.userIdx = U.userIdx\n" +
                "left join (select followerIdx  from Follow WHERE status = 'ACTIVE' and followingIdx = ?) F on F.followerIdx = U.userIdx\n" +
                "where CommentLike.status = 'ACTIVE'\n" +
                "and CommentLike.commentIdx = ? ";
        Object[] getCommentLikeListParams = new Object[]{userIdx, commentIdx};
        return this.jdbcTemplate.query(getCommentLikeListQuery,
                (rs,rowNum) -> new GetCommentLikeListRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("userId"),
                        rs.getString("profileImg"),
                        rs.getString("followStatus")),
                getCommentLikeListParams
        );
    }

    public GetCommentReportRes getCommentReportByUserIdxCommentIdx(int commentIdx, int userIdx) {
        try {
            String getCommentQuery = "select * from CommentReport where commentIdx = ? and userIdx = ?";
            Object[] getCommentReportParams = new Object[]{commentIdx, userIdx};
            return this.jdbcTemplate.queryForObject(getCommentQuery,
                    (rs, rowNum) -> new GetCommentReportRes(
                            rs.getInt("commentReportIdx"),
                            rs.getInt("commentIdx"),
                            rs.getInt("userIdx"),
                            rs.getString("status")),
                    getCommentReportParams);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int createCommentReport(int commentIdx, int userIdx, PostCommentReportReq postCommentReportReq) {
        String createCommentReportQuery = "insert into CommentReport (commentIdx, userIdx, reason) VALUES (?,?,?)";
        Object[] createCommentReportParams = new Object[]{commentIdx, userIdx, postCommentReportReq.getReason()};
        this.jdbcTemplate.update(createCommentReportQuery, createCommentReportParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
