package com.example.demo.src.profile;

import com.example.demo.src.post.model.GetPostConmmentRes;
import com.example.demo.src.post.model.GetPostImgRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.profile.model.GetProfileEditRes;
import com.example.demo.src.profile.model.GetProfileUserRes;
import com.example.demo.src.profile.model.GetProfiletImgRes;
import com.example.demo.src.profile.model.PatchProfileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProfileDao {
    private JdbcTemplate jdbcTemplate;
    private List<GetProfiletImgRes> profiletImgRes;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetProfileEditRes getUsersByUserIdx(int userIdx) {
        try{
            String getUserQuery = "select * from User where userIdx = ?";
            int getUserParams = userIdx;
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetProfileEditRes(
                            rs.getInt("userIdx"),
                            rs.getString("userId"),
                            rs.getString("name"),
                            rs.getString("profileImg"),
                            rs.getString("webSite"),
                            rs.getString("introduction")),
                    getUserParams);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int checkUserId(String userId, int userIdx){
        String checkUserIdQuery = "select exists(select userId from User where userId = ? and userIdx != ?) ";
        Object[] checkUserParams = new Object[]{userId, userIdx};
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserParams);

    }

    public int updateUsersByUserIdx(int userIdx, PatchProfileReq patchProfileReq) {
        String updateProfileQuery = "update User set userId = ?, name = ?, profileImg = ?, webSite = ?, introduction = ? where userIdx = ?";
        Object[] checkUserParams = new Object[]{patchProfileReq.getUserId(), patchProfileReq.getName(), patchProfileReq.getProfileImg(), patchProfileReq.getWebsite(), patchProfileReq.getIntroduction(), userIdx};

        return this.jdbcTemplate.update(updateProfileQuery,checkUserParams);
    }

    public GetProfileUserRes getProfileUserByUserIdx(int userIdx) {
        String getProfilesQuery = "select U.userIdx, U.userId, U.name, U.introduction, U.profileImg, U.webSite,\n" +
                "IF(postCount is null, 0, postCount) as postCount,\n" +
                "IF(followerCount is null, 0, followerCount) as followerCount,\n" +
                "IF(followingCount is null, 0, followingCount) as followingCount\n" +
                "from User as U\n" +
                "left join (select userIdx, count(postIdx) as postCount from Post where status = 'ACTIVE' group by userIdx) post on post.userIdx = U.userIdx\n" +
                "left join (select followingIdx, count(followerIdx) as followerCount from Follow where status = 'ACTIVE' group by followingIdx) follower on follower.followingIdx = U.userIdx\n" +
                "left join (select followerIdx, count(followingIdx) as followingCount from Follow where status = 'ACTIVE' group by followerIdx) following on following.followerIdx = U.userIdx\n" +
                "where U.userIdx = ?";
        int getProfilesByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getProfilesQuery,
                (rs,rowNum) -> new GetProfileUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("introduction"),
                        rs.getString("profileImg"),
                        rs.getString("webSite"),
                        rs.getInt("postCount"),
                        rs.getInt("followerCount"),
                        rs.getInt("followingCount"),
                        null,
                        profiletImgRes = this.jdbcTemplate.query("select Post.postIdx, min(postImgUrl) as postImgUrl\n" +
                                        "from Post\n" +
                                        "left join PostImg p on Post.postIdx = p.postIdx\n" +
                                        "left join User u on Post.userIdx = u.userIdx\n" +
                                        "where u.userIdx = ?\n" +
                                        "group by Post.postidx",
                                (rk, rownum) -> new GetProfiletImgRes(
                                        rk.getInt("postIdx"),
                                        rk.getString("postImgUrl")
                                ), userIdx)
                ),
                getProfilesByIdxParams);
    }
}

