package com.example.demo.src.search;

import com.example.demo.src.search.model.GetSearchListRes;
import com.example.demo.src.search.model.GetSearchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createSearch(int userIdx, String word) {
        String createSearchQuery = "insert into Search (userIdx, searchWord) VALUES (?,?)";
        Object[] createSearchParams = new Object[]{userIdx, word};
        this.jdbcTemplate.update(createSearchQuery, createSearchParams);

    }

    public List<GetSearchRes> searchByUsers(String word){
        String searchByUsersQuery = "select u.userIdx, u.userId, u.name, u.profileImg, count(p.content) as cnt from User u " +
                "left join Post p on u.userIdx = p.userIdx " +
                "where u.userId like ? " +
                "group by u.userIdx";
        String searchByUsersParam = word + "%";
        return this.jdbcTemplate.query(searchByUsersQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("userIdx"),
                        rs.getString("userId"), // userId
                        rs.getString("name"), // name
                        rs.getString("profileImg"),
                        rs.getInt("cnt")
                ), searchByUsersParam
        );
    }

    public List<GetSearchRes> searchByLocations(String word){
        String searchByLocationsQuery = "select locationIdx, name, location, locationImgUrl, '100' as cnt from Location " +
                "where name like ? or location like ?";
        Object[] searchByLocationsParam = new Object[]{"%" +word + "%", "%" +word + "%"};
        return this.jdbcTemplate.query(searchByLocationsQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("locationIdx"),
                        rs.getString("name"), // name
                        rs.getString("location"), // location
                        rs.getString("locationImgUrl"),
                        rs.getInt("cnt")
                ), searchByLocationsParam
        );
    }

    public List<GetSearchRes> searchByTags(String word){
        String searchByTagsQuery = "select tagIdx, content, status, tagImgUrl, '100' as cnt from Tag " +
                "where content like ?";
        String searchByTagsParam = word + "%";
        return this.jdbcTemplate.query(searchByTagsQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("tagIdx"),
                        rs.getString("content"), // content
                        rs.getString("status"), // status
                        rs.getString("tagImgUrl"),
                        rs.getInt("cnt")
                ), searchByTagsParam
        );
    }

    public List<GetSearchListRes> getSearchList(int userIdx){
        String getSearchListQuery = "select distinct(searchWord) as searchWord, status from Search where userIdx = ?";
        int getSearchListParam = userIdx;
        return this.jdbcTemplate.query(getSearchListQuery,
                (rs,rowNum) -> new GetSearchListRes(
                        rs.getString("searchWord"),
                        rs.getString("status")
                ), getSearchListParam
        );
    }

    public int deleteSearchList(int userIdx) {
        String deleteSearchListQuery = "delete from Search where userIdx = ?";
        int deleteSearchListParams = userIdx;

        return this.jdbcTemplate.update(deleteSearchListQuery,deleteSearchListParams);
    }

    public int deleteSearchWord(int userIdx, String word) {
        String deleteSearchWordQuery = "delete from Search where userIdx = ? and searchWord = ?";
        Object[] deleteSearchWordParams = new Object[]{userIdx, word};

        return this.jdbcTemplate.update(deleteSearchWordQuery,deleteSearchWordParams);
    }
}
