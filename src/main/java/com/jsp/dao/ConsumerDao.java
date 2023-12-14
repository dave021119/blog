package com.jsp.dao;

import com.jsp.form.UserForm;
import com.jsp.tool.JDBConnection;
import com.jsp.tool.StrMD5;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户查询
 */
public class ConsumerDao {
    private JDBConnection connection = null;
    private UserForm consumerForm = null;

    public ConsumerDao () {
        this.connection = new JDBConnection ();
    }



    /**
     * 添加一个用户数据
     *
     * @param form 数据
     * @return 添加结果
     */
    public boolean addConsumerForm (UserForm form) {
        boolean flag = false;
        String sql = "insert into tb_consumer (account,password,name,role)values ('" + form.getAccount () + "','" + StrMD5.MD5 (form.getPassword ()) + "','" + form.getName ()  + "','" + form.getRole ()  + "')";
        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }
        return flag;
    }


    /**
     * 获得数据
     *
     * @param account 账号
     * @return 结果
     */
    public UserForm getConsumerForm (String account) {
        String sql = "select * from tb_consumer where account='" + account + "'";

        try {
            ResultSet rs = this.connection.executeQuery (sql);
            while (rs.next ()) {
                this.consumerForm = new UserForm();
                this.consumerForm.setId (Integer.valueOf (rs.getString (1)));
                this.consumerForm.setAccount (rs.getString (2));
                this.consumerForm.setPassword (rs.getString (3));
                this.consumerForm.setName (rs.getString (4));
            }
        } catch (SQLException var4) {
            var4.printStackTrace ();
        }
        return this.consumerForm;
    }



    /**
     * @param username 用户名
     * @param password 用户密码
     * @return 返回一个数据
     */
    public UserForm login (String username, String password) {
        this.consumerForm = new UserForm();
        String sql = "select * from tb_consumer where account='" + username + "' and password='" + password + "'";
        try {
            ResultSet rs = this.connection.executeQuery (sql);
            while (rs.next ()) {
                this.consumerForm.setId (Integer.valueOf (rs.getString (1)));
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


    /**
     * 获得链接数量
     *
     * @return 链接数量
     */
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
        ConsumerDao consumerDao = new ConsumerDao ();
        System.out.println (consumerDao.getConsumerForm ("13278731317"));
    }
}
