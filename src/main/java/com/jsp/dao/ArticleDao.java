package com.jsp.dao;

import com.jsp.form.ArticleForm;
import com.jsp.tool.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    private JDBConnection connection = null;
    private ArticleForm articleForm = null;

    public ArticleDao () {
        this.connection = new JDBConnection ();
    }

    /**
     * 数据实现增删改查
     *
     * @param operation 操作
     * @param form      你要操作的对象
     * @return 操作结果
     */
    public boolean operationArticle (String operation, ArticleForm form) {
        boolean flag = false;
        String sql = null;
        if ("添加".equals (operation)) {
            sql = "insert into tb_article values ('" + form.getTypeId () + "','" + form.getTitle () + "','" + form.getContent () + "','" + form.getPhTime () + "','" + form.getNumber () + "','" + form.getImage () + "')";
        }

        if ("修改".equals (operation)) {
            sql = "update tb_article set typeID='" + form.getTypeId () + "',title='" + form.getTitle () + "',image='" + form.getImage () + "',content='" + form.getContent () + "' where id='" + form.getId () + "'";
        }

        if ("删除".equals (operation)) {
            sql = "delete from tb_article where id='" + form.getId () + "'";
        }

        if ("增加".equals (operation)) {
            sql = "update tb_article set number=number+1 where id='" + form.getId () + "'";
        }

        if ("addVote".equals (operation)) {
            sql = "update tb_article set voteNumber=voteNumber+1 where id='" + form.getId () + "'";
        }

        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }

    /**
     * 根据ID 查询文章根据种类
     *
     * @param typeId 文章ID 没有的话就是查询全部的文章
     * @return 返回文章集合
     */
    public List<ArticleForm> queryArticle (Integer typeId) {
        List<ArticleForm> list = new ArrayList<> ();
        String sql = null;
        if (typeId == null) {
            sql = "select * from tb_article";
        } else {
            sql = "select * from tb_article where typeID='" + typeId + "' order by id desc";
        }
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.articleForm = new ArticleForm ();
                //根据列的编号获得数据
                this.articleForm.setId (rs.getInt (1));
                this.articleForm.setTypeId (rs.getInt (2));
                this.articleForm.setTitle (rs.getString (3));
                this.articleForm.setContent (rs.getString (4));
                this.articleForm.setPhTime (rs.getString (5));
                this.articleForm.setNumber (rs.getInt (6));
                this.articleForm.setImage (rs.getString (7));
                this.articleForm.setVoteNumber (rs.getInt (8));
                this.articleForm.setStatus (rs.getInt (9));
                list.add (this.articleForm);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return list;
    }

    /**
     * 根据ID查询博客
     *
     * @param id 博客ID
     * @return 博客
     */
    public ArticleForm queryArticleForm (Integer id) {
        String sql = "select * from tb_article where id='" + id + "'";
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.articleForm = new ArticleForm ();
                this.articleForm.setId (rs.getInt (1));
                this.articleForm.setTypeId (rs.getInt (2));
                this.articleForm.setTitle (rs.getString (3));
                this.articleForm.setContent (rs.getString (4));
                this.articleForm.setPhTime (rs.getString (5));
                this.articleForm.setNumber (rs.getInt (6));
                this.articleForm.setImage (rs.getString (7));
                this.articleForm.setVoteNumber (rs.getInt (8));
                this.articleForm.setStatus (rs.getInt (9));
                this.articleForm.setUserId (rs.getInt (10));
            }
        } catch (SQLException var5) {
            var5.printStackTrace ();
        }
        //访客自动加1
        this.operationArticle ("增加", this.articleForm);
        return this.articleForm;
    }

    /**
     * 根据条件查询数据
     *
     * @param where 查询语句
     * @return 查询结果
     */
    public List<ArticleForm> queryArticleWhere (String where) {
        List<ArticleForm> list = new ArrayList<> ();
        String sql = null;
        if (where == null) {
            sql = "select * from tb_article order by phTime desc";
        } else {
            sql = "select * from tb_article where " + where;
        }
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.articleForm = new ArticleForm ();
                //根据列的编号获得数据
                this.articleForm.setId (rs.getInt (1));
                this.articleForm.setTypeId (rs.getInt (2));
                this.articleForm.setTitle (rs.getString (3));
                this.articleForm.setContent (rs.getString (4));
                this.articleForm.setPhTime (rs.getString (5));
                this.articleForm.setNumber (rs.getInt (6));
                this.articleForm.setImage (rs.getString (7));
                this.articleForm.setVoteNumber (rs.getInt (8));
                this.articleForm.setStatus (rs.getInt (9));
                list.add (this.articleForm);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return list;
    }

    /**
     * 更新一个博客数据
     *
     * @param articleForm 博客
     * @return 更新结果
     */
    public boolean updateArticle (ArticleForm articleForm) {
        String sql = "update tb_article t\n" +
                "set title='" + articleForm.getTitle () + "'," +
                "    content='" + articleForm.getContent () + "'," +
                "    image='" + articleForm.getImage () + "'," +
                "    status='" + articleForm.getStatus () + "'," +
                "    typeID='" + articleForm.getTypeId () + "'" +
                "where id='" + articleForm.getId () + "'";
        return this.connection.executeUpdate (sql);
    }

    public boolean addBlog (ArticleForm articleForm) {
        String sql = "insert into tb_article ( typeID, title, content, phTime, number, image, voteNumber, status,userId)\n" +
                "values ('"
                + articleForm.getTypeId () + "','"
                + articleForm.getTitle () + "','"
                + articleForm.getContent () + "','"
                + articleForm.getPhTime () + "','"
                + articleForm.getNumber () + "','"
                + articleForm.getImage () + "','"
                + articleForm.getVoteNumber () + "','"
                + articleForm.getStatus () + "','"
                + articleForm.getUserId () + "');";
        return this.connection.executeUpdate (sql);
    }

    /**
     * 更新一个博客数据的评论数
     *
     * @param articleForm 博客
     * @return 更新结果
     */
    public boolean updateArticleVote (ArticleForm articleForm) {
        String sql = "update tb_article t\n" +
                "set voteNumber=voteNumber-1 " +
                "where id=" + articleForm.getId ();
        return this.connection.executeUpdate (sql);
    }

    /**
     * 获得文章数量
     *
     * @return 文章数量
     */
    public int getCount () {
        int a = 0;
        String sql = "select count(*) from tb_article";
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

    public static void main (String[] args) {
        ArticleDao articleDao = new ArticleDao ();
        System.out.println (articleDao.getCount ());
    }
}
