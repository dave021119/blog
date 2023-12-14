
package com.jsp.tool;


import java.sql.Connection;
/*    */
import java.sql.ResultSet;
import java.sql.SQLException;
/*    */
import java.sql.Statement;

public class JDBConnection {
    private Connection con = null;

    static {
        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println ("数据库加载失败");
        }
    }

    public JDBConnection () {
    }

    public void creatConnection () {
        try {
            con = java.sql.DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root", "lkhdsg");
            con.setAutoCommit (true);
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("creatConnectionError!");
        }
    }

    /**
     * 修改语句 insert update delete
     *
     * @param sql sql 语句
     * @return 返回修改结果
     */
    public boolean executeUpdate (String sql) {
        System.out.println ("修改语句====" + sql);
        if (con == null) {
            creatConnection ();
        }
        try {
            Statement stmt = con.createStatement ();
            int iCount = stmt.executeUpdate (sql);
            System.out.println ("操作成功，所影响的记录数为" + String.valueOf (iCount));
            return true;
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
        }
        return false;
    }

    /**
     * 查询结果
     *
     * @param sql sql语句
     * @return 查询结果
     */
    public java.sql.ResultSet executeQuery (String sql) {
        System.out.println ("查询语句====" + sql);
        java.sql.ResultSet rs;
        try {
            if (con == null) {
                creatConnection ();
            }
            Statement stmt = con.createStatement ();
            try {
                rs = stmt.executeQuery (sql);
            } catch (SQLException e) {
                System.out.println (e.getMessage ());
                return null;
            }
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("executeQueryError!");
            return null;
        }
        return rs;
    }

    /**
     * 创建链接，带有事务的特性
     */
    public void creatConnectionWithTransaction () {
        try {
            con = java.sql.DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "lkhdsg");
            //关闭自动提交，如果失败那么就回滚
            con.setAutoCommit (false);
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("creatConnectionError!");
        }
    }

    /**
     * 查询语句
     *
     * @param sql sql 语句
     * @return 返回查询结果
     */
    public boolean executeUpdateWithTransaction (String sql) throws SQLException {
        if (con == null) {
            //开启一个带有事务的链接
            creatConnectionWithTransaction ();
        }
        try {
            Statement stmt = con.createStatement ();
            int iCount = stmt.executeUpdate (sql);
            System.out.println ("操作成功，所影响的记录数为" + String.valueOf (iCount));
            con.commit ();
            return true;
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("操作失败，元数据已经回滚");
            con.rollback ();
        }
        return false;
    }

    /**
     * 对数据库的资源进行释放
     *
     * @param connection 数据库的连接
     * @param statement  数据库的sql操作实体，操作增删改查的对象
     * @param resultSet  如果是数据库的查询语言，那么就会产生这个对象
     */
    public static void release (Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (connection != null) {
                connection.close ();
            }
            if (statement != null) {
                statement.close ();
            }
            if (resultSet != null) {
                resultSet.close ();
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
