package com.jsp.dao;

import com.jsp.form.DataForm;
import com.jsp.tool.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataDao {
    private JDBConnection connection = null;
    private DataForm data = null;

    public DataDao() {
        this.connection = new JDBConnection ();
    }

    /**
     * 数据实现增删改查
     *
     * @param operation 操作
     * @param form      你要操作的对象
     * @return 操作结果
     */
    public boolean operationData (String operation, DataForm form) {
        boolean flag = false;
        String sql = null;
        if ("添加".equals (operation)) {
            sql = "insert into tb_data values ('" + form.getUserId () + "','" + form.getaId() + "','" + form.getaId () +"')";
        }

        if ("修改".equals (operation)) {
            sql = "update tb_data set userId='" + form.getUserId () + "',aId='" + form.getaId () + "',type='" + form.getaId () +"'";
        }

        if ("删除".equals (operation)) {
            sql = "delete from tb_data where id='" + form.getId () + "'";
        }


        if (this.connection.executeUpdate (sql)) {
            flag = true;
        }

        return flag;
    }



    public DataForm queryDataForm (Integer id) {
        String sql = "select * from tb_data where id='" + id + "'";
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.data = new DataForm();
                this.data.setId (rs.getInt (1));
                this.data.setUserId (rs.getInt (2));
                this.data.setaId (rs.getInt (3));
                this.data.setType (rs.getInt (4));
            }
        } catch (SQLException var5) {
            var5.printStackTrace ();
        }

        return this.data;
    }



    public List<DataForm> queryDataWhere (String where) {
        List<DataForm> list = new ArrayList<> ();
        String sql = null;
        if (where == null) {
            sql = "select * from tb_data ";
        } else {
            sql = "select * from tb_data where " + where;
        }
        ResultSet rs = this.connection.executeQuery (sql);
        try {
            while (rs.next ()) {
                this.data = new DataForm();
                //根据列的编号获得数据
                this.data.setId (rs.getInt (1));
                this.data.setUserId (rs.getInt (2));
                this.data.setaId (rs.getInt (3));
                this.data.setType (rs.getInt (4));

                list.add (this.data);
            }
        } catch (SQLException var6) {
            var6.printStackTrace ();
        }
        return list;
    }


    public boolean updateData(DataForm data) {
        String sql = "update tb_data t\n" +
                "set userId='" + data.getUserId() + "'," +
                "    aId='" + data.getaId() + "'," +
                "    type='" + data.getType() + "' " +
                "where id='" + data.getId() + "'";
        return this.connection.executeUpdate(sql);
    }


    public boolean addData (DataForm data) {
        String sql = "insert into tb_data ( userId, aId, type)\n" +
                "values ('"
                + data.getUserId () + "','"
                + data.getaId () + "','"
                + data.getType () + "');";
        return this.connection.executeUpdate (sql);
    }



    public int getCount () {
        int a = 0;
        String sql = "select count(*) from tb_data";
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

    public int getBlogCount (String s) {
        int a = 0;
        String sql = "select count(*) from tb_data where "+s;
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
        DataDao DataDao = new DataDao();
        System.out.println (DataDao.getCount ());
    }
}
