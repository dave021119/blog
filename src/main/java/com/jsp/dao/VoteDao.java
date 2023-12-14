package com.jsp.dao;

import com.jsp.form.VoteForm;
import com.jsp.tool.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoteDao {
    private JDBConnection connection = null;

    public VoteDao () {
        this.connection = new JDBConnection ();
    }

    public boolean operationVote (String operation, VoteForm voteForm) {
        boolean flag = false;
        String sql = null;
        if (operation.equals ("删除")) {
            sql = "delete from tb_vote where id='" + voteForm.getId () + "'";
        }

        if (operation.equals ("添加")) {
            sql = "insert into tb_vote values('" + voteForm.getVoteName () + "','" + voteForm.getVoteTime () + "','" + voteForm.getVoteContent () + "','" + voteForm.getVoteLink () + "','" + voteForm.getEmail () + "')";
        }

        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }

    public List<VoteForm> queryVoteList (String where) {
        List<VoteForm> list = new ArrayList<> ();
        String sql = "select * from tb_vote where " + where;
        ResultSet rs = this.connection.executeQuery (sql);
        VoteForm form = null;

        try {
            while (rs.next ()) {
                form = new VoteForm ();
                form.setId (Integer.valueOf (rs.getString (1)));
                form.setVoteName (rs.getString (2));
                form.setVoteTime (rs.getString (3));
                form.setVoteContent (rs.getString (4));
                form.setVoteLink (rs.getString (5));
                form.setEmail (rs.getString (6));
                list.add (form);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return list;
    }

    /**
     * 添加一条评论
     *
     * @param voteForm 评论
     * @return 返回添加结果
     */
    public boolean addVote (VoteForm voteForm) {
        boolean flag = false;
        String sql = "insert into tb_vote (voteName, voteTime, voteContent, voteLink,email)\n" +
                "values " +
                "('" + voteForm.getVoteName () + "','" + voteForm.getVoteTime () + "','" + voteForm.getVoteContent () + "','" + voteForm.getVoteLink () + "','" + voteForm.getEmail () + "');";
        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除博客评论
     *
     * @param voteForm 博客评论
     * @return 删除结果
     */
    public boolean delVote (VoteForm voteForm) {
        String sql = "delete from tb_vote where id='" + voteForm.getId () + "'";
        return this.connection.executeUpdate (sql);
    }

    /**
     * 获得评论数量
     *
     * @return 文章数量
     */
    public int getCount () {
        int a = 0;
        String sql = "select count(*) from tb_vote";
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                a = rs.getInt (1);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return a;
    }
}
