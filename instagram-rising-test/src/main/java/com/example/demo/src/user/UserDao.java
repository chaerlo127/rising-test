package com.example.demo.src.user;


import com.example.demo.src.profile.model.GetPersonInfo;
import com.example.demo.src.profile.model.GetProfileInfo;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select userIdx, name, userId, email, phone from User where status = 'ACTIVE'";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("userId"),
                        rs.getString("email"),
                        rs.getString("phone"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from User where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")),
                getUserParams);
    }

    public int getUserIdxByEmailOrPhone(String email, String phone){
        String getUserIdxQuery = "select userIdx from User where email = ? or phone = ?";
        Object[] getUserIdxParams = new Object[]{email, phone};
        return this.jdbcTemplate.queryForObject(getUserIdxQuery,
                int.class,
                getUserIdxParams);
    }


    public GetUserRes getUserByUserIdx(int userIdx){
        try{
            String getUserQuery = "select * from User where userIdx = ?";
            int getUserParams = userIdx;
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetUserRes(
                            rs.getInt("userIdx"),
                            rs.getString("userId"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone")),
                    getUserParams);

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userId, password, name, phone, email, birth) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), postUserReq.getPassword(), postUserReq.getName(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getBirth()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkPhone(String phone){
        String checkPhoneQuery = "select exists(select phone from User where phone = ?)";
        String checkPhoneParams = phone;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);

    }

    public int checkUserId(String userId){
        String checkUserIdQuery = "select exists(select userId from User where userId = ?)";
        String checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);

    }

    public String checkUserStatus(String email, String phone){
        String checkUserStatusQuery = "select status from User where email = ? or phone = ?";
        Object[] checkUserStatusParams = new Object[]{email, phone};
        return this.jdbcTemplate.queryForObject(checkUserStatusQuery,
                String.class,
                checkUserStatusParams);

    }

    public int withdrawUser(int userIdx){
        String withdrawUserQuery = "update User set status = 'INACTIVE' where userIdx = ? and status = 'ACTIVE'";
        int withdrawUserParams = userIdx;

        return this.jdbcTemplate.update(withdrawUserQuery,withdrawUserParams);
    }

    public int modifyUserLoginStatus(int userIdx, String loginStatus){
        String modifyUserActiveQuery = "update User set loginStatus = ? where userIdx = ?";
        Object[] modifyUserActiveParams = new Object[]{loginStatus, userIdx};

        return this.jdbcTemplate.update(modifyUserActiveQuery,modifyUserActiveParams);
    }

    public int resetUserPassword(PatchUserReq patchUserReq){
        String resetUserPasswordQuery = "update User set password = ? where userId = ? ";
        Object[] resetUserPasswordParams = new Object[]{patchUserReq.getNewPassword(), patchUserReq.getUserId()};

        return this.jdbcTemplate.update(resetUserPasswordQuery,resetUserPasswordParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password, email, name, userId from User where email = ? or phone = ?";
        Object[] getPwdParams = new Object[]{postLoginReq.getEmail(), postLoginReq.getPhone()};

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("userId")
                ),
                getPwdParams
                );

    }


    public GetProfileInfo getProfileInfoByUserIdx(int userIdx) {
        try{
            String getUserQuery = "select * from User where userIdx = ?";
            int getUserParams = userIdx;
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetProfileInfo(
                            rs.getInt("userIdx"),
                            rs.getString("userId"),
                            rs.getString("createAt"),
                            rs.getString("nation")),
                    getUserParams);

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int patchUserInfoByUserIdx(int userIdx, GetPersonInfo getPersonInfo) {
        String updateUserInfoQuery = "update User set email = ?, phone = ?, sex = ?, birth = ? where userIdx = ?";
        Object[] checkUserInfoParams = new Object[]{getPersonInfo.getEmail(), getPersonInfo.getPhone(), getPersonInfo.getSex(), getPersonInfo.getBirth(), userIdx};

        return this.jdbcTemplate.update(updateUserInfoQuery,checkUserInfoParams);
    }

    public GetPersonInfo getUserInfoByUserIdx(int userIdx) {
        try{
            String getUserQuery = "select * from User where userIdx = ?";
            int getUserParams = userIdx;
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetPersonInfo(
                            rs.getInt("userIdx"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("sex"),
                            rs.getString("birth")),
                    getUserParams);

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int modifyUserId(int userIdx, PatchUserIdReq patchUserIdReq){
        String modifyUserIdQuery = "update User set userId = ? where userIdx = ?";
        Object[] modifyUserIdParams = new Object[]{patchUserIdReq.getNewUserId(), userIdx};

        return this.jdbcTemplate.update(modifyUserIdQuery,modifyUserIdParams);
    }
}
