package com.example.demo.src.highlight;

import com.example.demo.src.highlight.model.GetHighTagUsersRes;
import com.example.demo.src.highlight.model.GetHighlightsRes;
import com.example.demo.src.highlight.model.PostHighlightReq;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HighlightDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int maxIdx() {
        String checkLikeQuery = "select ifnull(max(groupIdx),0) from Highlight";
        return this.jdbcTemplate.queryForObject(checkLikeQuery, int.class);
    }
    public int createHighlights(int userIdx, int maxGroupIdx, PostHighlightReq postHighlightReq) {
        for (int i = 0; i < postHighlightReq.getStoryIdx().size(); i++) {
            String createStoryQuery = "insert into Highlight (groupIdx, groupName, userIdx, storyIdx, coverImg) VALUES (?,?,?,?,?)";
            Object[] createStoryParams = new Object[]{maxGroupIdx, postHighlightReq.getName(), userIdx,
                    postHighlightReq.getStoryIdx().get(i), postHighlightReq.getCoverImg()};
            this.jdbcTemplate.update(createStoryQuery, createStoryParams);
        }
        return maxGroupIdx;
    }

    public List<GetHighlightsRes> getHighlight(int userIdx) {
        try{
            String getHighlightQuery = "select userIdx, groupIdx, coverImg, groupName from Highlight where userIdx = ? group by groupIdx, coverImg, groupName";
            int getHighlightParams = userIdx;
            return this.jdbcTemplate.query(getHighlightQuery,
                    (rs, rowNum) -> new GetHighlightsRes(
                            rs.getInt("userIdx"),
                            rs.getInt("groupIdx"),
                            rs.getString("coverImg"),
                            rs.getString("groupName")),
                    getHighlightParams);

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
