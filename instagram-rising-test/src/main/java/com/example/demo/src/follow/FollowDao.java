package com.example.demo.src.follow;

import com.example.demo.src.follow.model.GetFollowStatusRes;
import com.example.demo.src.follow.model.GetFollowsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createFollow(int userIdx, String followIdx) {
        String createFollowQuery = "insert into Follow (followingIdx, followerIdx) values (?,?)";
        Object[] createFollowParams = new Object[]{userIdx, followIdx};
        return this.jdbcTemplate.update(createFollowQuery, createFollowParams);
    }

    public List<GetFollowsRes> getFollowers(int userIdx) {
        String getFollowersQuery = "select u.useridx, u.name, u.profileImg, u.userId, u.inActive, f.status\n" +
                "from User u inner join Follow f on u.userIdx = f.followingIdx \n" +
                "where f.followerIdx = ?";
        int getFollowersParam = userIdx;
        return this.jdbcTemplate.query(getFollowersQuery,
                (rs, rowNum) -> new GetFollowsRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("profileImg"),
                        rs.getString("userId"),
                        rs.getString("inActive"),
                        rs.getString("status")
                ), getFollowersParam
        );
    }

    public List<GetFollowsRes> getFollowings(int userIdx) {
        String getFollowersQuery = "select u.useridx, u.name, u.profileImg, u.userId, u.inActive, f.status\n" +
                "from User u inner join Follow f on u.userIdx = f.followerIdx \n" +
                "where f.followingIdx = ?";
        int getFollowersParam = userIdx;
        return this.jdbcTemplate.query(getFollowersQuery,
                (rs, rowNum) -> new GetFollowsRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("profileImg"),
                        rs.getString("userId"),
                        rs.getString("inActive"),
                        rs.getString("status")
                ), getFollowersParam
        );
    }

    public int modifyFollow(int userIdx, String followIdx, String status) {
        String modifyFollowQuery = "update Follow set status = ? where followingIdx = ? and followerIdx = ?";
        Object[] modifyFollowParams = new Object[]{status, userIdx, followIdx};
        return this.jdbcTemplate.update(modifyFollowQuery, modifyFollowParams);
    }

    public int checkFollow(int userIdx, String followIdx) {
        String checkFollowQuery = "select exists(select followidx from Follow where followingIdx = ? and followerIdx = ?)";
        Object[] checkFollowParams = new Object[]{userIdx, followIdx};
        return this.jdbcTemplate.queryForObject(checkFollowQuery,
                int.class,
                checkFollowParams);
    }

    public GetFollowStatusRes getFollowStatus(int follower, int userIdx) {
        String checkFollowQuery = "select status\n" +
                "from Follow\n" +
                "where followingIdx = ? and followerIdx = ?";
        Object[] checkFollowParams = new Object[]{userIdx, follower};
        return this.jdbcTemplate.queryForObject(checkFollowQuery,
                (rs, rowNum) -> new GetFollowStatusRes(
                        rs.getString("status")
                ), checkFollowParams
        );
    }

    public List<GetFollowsRes> getRecommends(int userIdx) {
        String getRecommendsQuery = "select userIdx, name, profileImg, userId, inActive, 'INACTIVE' as status from User" +
                " where userIdx in (" +
                "    select followerIdx from Follow" +
                "    where followerIdx != ?" +
                "      and status = 'ACTIVE'" +
                "      and  followingIdx in (select followerIdx from Follow where followingIdx = ? and status = 'ACTIVE')" +
                "      and followerIdx not in (select followerIdx from Follow where followingIdx = ? and status = 'ACTIVE')" +
                "    group by followerIdx" +
                "    having count(followerIdx) >= 2 " +
                "    )";
        Object[] getRecommendsParam = new Object[]{userIdx, userIdx, userIdx};
        return this.jdbcTemplate.query(getRecommendsQuery,
                (rs, rowNum) -> new GetFollowsRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("profileImg"),
                        rs.getString("userId"),
                        rs.getString("inActive"),
                        rs.getString("status")
                ), getRecommendsParam
        );
    }

    public List<GetFollowsRes> getTogether(int userIdx, int followIdx) {
        String getTogetherQuery = "select userIdx, name, profileImg, userId, inActive, 'ACTIVE' as status from User\n" +
                "where userIdx in ( select followingIdx from \n" +
                "(select followerIdx from Follow where followingIdx = ?) a \n" + // 나의 팔로워 목록
                "left join (select followingIdx from Follow where followerIdx = ?) b\n" + // 내가 보는 사람의 팔로잉 목록
                "on a.followerIdx = b.followingIdx\n" +
                "where b.followingIdx is not null)";
        Object[] getTogetherParam = new Object[]{userIdx, followIdx};
        return this.jdbcTemplate.query(getTogetherQuery,
                (rs, rowNum) -> new GetFollowsRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("profileImg"),
                        rs.getString("userId"),
                        rs.getString("inActive"),
                        rs.getString("status")
                ), getTogetherParam
        );
    }
}
