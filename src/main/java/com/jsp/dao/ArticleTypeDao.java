package com.jsp.dao;

import com.jsp.form.ArticleTypeForm;
import com.jsp.form.VoteForm;
import com.jsp.tool.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleTypeDao {
    private JDBConnection connection = null;

    public ArticleTypeDao () {
        this.connection = new JDBConnection ();
    }

    public boolean operationArticleType (String operation, ArticleTypeForm form) {
        boolean flag = false;
        String sql = null;
        if (operation.equals ("添加")) {
            sql = "insert into tb_articleType values ('" + form.getTypeName () + "','" + form.getDescription () + "')";
        }

        if (operation.equals ("删除")) {
            sql = "delete from tb_articleType where id='" + form.getId () + "'";
        }

        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }

    /**
     * 根据ID查询数据
     *
     * @param id 数据ID
     * @return 查询结果
     */
    public String queryArticleTypeName (Integer id) {
        String typeName = null;
        String sql = "select * from tb_articleType where id='" + id + "'";
        ResultSet rs = this.connection.executeQuery (sql);

        try {
            while (rs.next ()) {
                typeName = rs.getString ("typeName");
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }

        return typeName;
    }

    /**
     * 查询所有的博客种类
     *
     * @param where 带上where的条件查询
     * @return 博客种类列表
     */
    public List<ArticleTypeForm> queryArticleType (String where) {
        List<ArticleTypeForm> list = new ArrayList<> ();
        ArticleTypeForm form = null;
        String sql = "select * from tb_articleType" + where;
        ResultSet rs = this.connection.executeQuery (sql);

        try {
            while (rs.next ()) {
                form = new ArticleTypeForm ();
                form.setId (rs.getInt (1));
                form.setTypeName (rs.getString (2));
                form.setDescription (rs.getString (3));
                form.setNumber (rs.getInt (4));
                list.add (form);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }

        return list;
    }

    /**
     * 更新数据库字段
     *
     * @param type 要更新得数据对象
     * @return 返回更新结果
     */
    public boolean update (ArticleTypeForm type) {
        String sql = "update tb_articletype\n" +
                "set typeName = '" + type.getTypeName () + "' , number = '" + type.getNumber () + "' , description = '" + type.getDescription () + "'\n" +
                "where id = '" + type.getId () + "';";
        return this.connection.executeUpdate (sql);
    }

    /**
     * 添加一个博客种类
     *
     * @param articleTypeForm 博客种类
     * @return 添加结果
     */
    public boolean add (ArticleTypeForm articleTypeForm) {
        String sql = "insert into tb_articletype (typeName, description)\n" +
                "values ('"
                + articleTypeForm.getTypeName () + "','"
                + articleTypeForm.getDescription () + "')";
        return this.connection.executeUpdate (sql);
    }

    /**
     * 获得文章种类数量
     *
     * @return 文章种类数量
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
}
