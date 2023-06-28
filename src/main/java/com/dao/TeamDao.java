package com.dao;

import com.domain.Match;
import com.domain.Member;
import com.domain.Team;
import com.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TeamDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    //寻找某人所属团队
    public  Team  getTeamById(int teamId){
        Team team =new Team();
        try {
            String sql = "select * from team where Id = ?";
            team = template.queryForObject(sql, new BeanPropertyRowMapper<>(Team.class),teamId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return team;
        }
    }

    //创建团队成功时返回TeamId
    public int add(String teamName, String description, int matchId){
        int affectRows = 0;
        int teamId = 0;
        try {
            String sql = "insert into team (Name, isDelete, WorkName, Like, LV, Description, MatchId) values(?,0,'暂无',0,0,?,?)";
            affectRows = template.update(sql,teamName, description, matchId);
            if(affectRows > 0){
                String sql1 = "select Id from team where TeamName = ? and MatchId = ?";
                teamId = template.queryForObject(sql1, new BeanPropertyRowMapper<>(int.class),teamName, matchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return teamId;
        }
    }

    //验证是否重名
    public Team verifyName(String teamName, int matchId){
        Team team = null;
        try {
            String sql = "select * from team where TeamId = ? and MatchId = ?;";
            team = template.queryForObject(sql, new BeanPropertyRowMapper<>(Team.class),teamName, matchId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return team;
        }
    }

    //根据matchId寻找团队
    public  List<Team>  getTeamByMatchId(int matchId){
        List<Team> teamList =null;
        try {
            String sql = "select * from team where MatchId = ?";
            teamList = template.query(sql, new BeanPropertyRowMapper<>(Team.class),matchId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return teamList;
        }
    }

    //设置奖项
    public boolean setAward(int teamId, int lv ){
        int affectRows = 0;
        try {
            String sql = "update  team set Lv = ?, IsDelete = ?";
            affectRows = template.update(sql, teamId, 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return affectRows > 0;
        }
    }
}