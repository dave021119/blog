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
        if (operation.equals ("���")) {
            sql = "insert into tb_articleType values ('" + form.getTypeName () + "','" + form.getDescription () + "')";
        }

        if (operation.equals ("ɾ��")) {
            sql = "delete from tb_articleType where id='" + form.getId () + "'";
        }

        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }

    /**
     * ����ID��ѯ����
     *
     * @param id ����ID
     * @return ��ѯ���
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
     * ��ѯ���еĲ�������
     *
     * @param where ����where��������ѯ
     * @return ���������б�
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
     * �������ݿ��ֶ�
     *
     * @param type Ҫ���µ����ݶ���
     * @return ���ظ��½��
     */
    public boolean update (ArticleTypeForm type) {
        String sql = "update tb_articletype\n" +
                "set typeName = '" + type.getTypeName () + "' , number = '" + type.getNumber () + "' , description = '" + type.getDescription () + "'\n" +
                "where id = '" + type.getId () + "';";
        return this.connection.executeUpdate (sql);
    }

    /**
     * ���һ����������
     *
     * @param articleTypeForm ��������
     * @return ��ӽ��
     */
    public boolean add (ArticleTypeForm articleTypeForm) {
        String sql = "insert into tb_articletype (typeName, description)\n" +
                "values ('"
                + articleTypeForm.getTypeName () + "','"
                + articleTypeForm.getDescription () + "')";
        return this.connection.executeUpdate (sql);
    }

    /**
     * ���������������
     *
     * @return ������������
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
