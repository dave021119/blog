package com.jsp.dao;

import com.jsp.form.UserForm;
import com.jsp.tool.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDao {
    private JDBConnection connection = null;
    private UserForm consumerForm = null;

    public UserDao() {
        this.connection = new JDBConnection ();
    }

    /**
     * 数据实现增删改查
     *
     * @param operation 操作
     * @param form      你要操作的对象
     * @return 操作结果
     */
    public boolean operationUser (String operation, UserForm form) {
        boolean flag = false;
        String sql = null;
        if ("添加".equals (operation)) {
            sql = "insert into tb_consumer values ('" + form.getAccount () + "','" + form.getPassword () + "','" + form.getName () + "','" + form.getRole ()+ "','" + form.getCreateTime ()+ "','" + form.getEmail () +"')";
        }

        if ("修改".equals (operation)) {
            sql = "update tb_consumer set account='" + form.getAccount () + "',password='" + form.getPassword () + "',name='" + form.getName () + "',role='" + form.getRole ()+ form.getCreateTime ()+ "','" + form.getEmail () +"'";
        }

        if ("删除".equals (operation)) {
            sql = "delete from tb_consumer where id='" + form.getId () + "'";
        }


        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }



    public UserForm queryUserForm (Integer id) {
        String sql = "select * from tb_consumer where id='" + id + "'";
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.consumerForm = new UserForm();
                this.consumerForm.setId (rs.getInt (1));
                this.consumerForm.setAccount (rs.getString (2));
                this.consumerForm.setPassword (rs.getString (3));
                this.consumerForm.setName (rs.getString (4));
                this.consumerForm.setRole (rs.getString (5));
                this.consumerForm.setCreateTime (rs.getTimestamp (6));
                this.consumerForm.setEmail (rs.getString (7));
            }
        } catch (SQLException var5) {
            var5.printStackTrace ();
        }

        return this.consumerForm;
    }



    public List<UserForm> queryUserWhere (String where) {
        List<UserForm> list = new ArrayList<> ();
        String sql = null;
        if (where == null) {
            sql = "select * from tb_consumer ";
        } else {
            sql = "select * from tb_consumer where " + where;
        }
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.consumerForm = new UserForm();
                //根据列的编号获得数据
                this.consumerForm.setId (rs.getInt (1));
                this.consumerForm.setAccount (rs.getString (2));
                this.consumerForm.setPassword (rs.getString (3));
                this.consumerForm.setName (rs.getString (4));
                this.consumerForm.setRole (rs.getString (5));
                this.consumerForm.setCreateTime (rs.getTimestamp (6));
                this.consumerForm.setEmail (rs.getString (7));

                list.add (this.consumerForm);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return list;
    }


    public boolean updateUser(UserForm consumerForm) {
        String sql = "update tb_consumer t\n" +
                "set account='" + consumerForm.getAccount() + "'," +
                "    password='" + consumerForm.getPassword() + "'," +
                "    name='" + consumerForm.getName() + "'," +
                "    role='" + consumerForm.getRole() + "'," +
                "    email='" + consumerForm.getEmail() + "' " +
                "where id='" + consumerForm.getId() + "'";
        return this.connection.executeUpdate(sql);
    }


    public boolean addUser (UserForm consumerForm) {
        String sql = "insert into tb_consumer ( account, password, name,role,email)\n" +
                "values ('"
                + consumerForm.getAccount () + "','"
                + consumerForm.getPassword () + "','"
                + consumerForm.getName () + "','"
                + consumerForm.getRole () + "','"
                + consumerForm.getEmail () + "');";
        return this.connection.executeUpdate (sql);
    }



    public int getCount () {
        int a = 0;
        String sql = "select count(*) from tb_consumer";
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
        UserDao UserDao = new UserDao();
        System.out.println (UserDao.getCount ());
    }
}
