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
     * ����ʵ����ɾ�Ĳ�
     *
     * @param operation ����
     * @param form      ��Ҫ�����Ķ���
     * @return �������
     */
    public boolean operationArticle (String operation, ArticleForm form) {
        boolean flag = false;
        String sql = null;
        if ("���".equals (operation)) {
            sql = "insert into tb_article values ('" + form.getTypeId () + "','" + form.getTitle () + "','" + form.getContent () + "','" + form.getPhTime () + "','" + form.getNumber () + "','" + form.getImage () + "')";
        }

        if ("�޸�".equals (operation)) {
            sql = "update tb_article set typeID='" + form.getTypeId () + "',title='" + form.getTitle () + "',image='" + form.getImage () + "',content='" + form.getContent () + "' where id='" + form.getId () + "'";
        }

        if ("ɾ��".equals (operation)) {
            sql = "delete from tb_article where id='" + form.getId () + "'";
        }

        if ("����".equals (operation)) {
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
     * ����ID ��ѯ���¸�������
     *
     * @param typeId ����ID û�еĻ����ǲ�ѯȫ��������
     * @return �������¼���
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
                //�����еı�Ż������
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
     * ����ID��ѯ����
     *
     * @param id ����ID
     * @return ����
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
        //�ÿ��Զ���1
        this.operationArticle ("����", this.articleForm);
        return this.articleForm;
    }

    /**
     * ����������ѯ����
     *
     * @param where ��ѯ���
     * @return ��ѯ���
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
                //�����еı�Ż������
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
     * ����һ����������
     *
     * @param articleForm ����
     * @return ���½��
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
     * ����һ���������ݵ�������
     *
     * @param articleForm ����
     * @return ���½��
     */
    public boolean updateArticleVote (ArticleForm articleForm) {
        String sql = "update tb_article t\n" +
                "set voteNumber=voteNumber-1 " +
                "where id=" + articleForm.getId ();
        return this.connection.executeUpdate (sql);
    }

    /**
     * �����������
     *
     * @return ��������
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
