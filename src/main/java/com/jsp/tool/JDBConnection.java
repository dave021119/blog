
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
            System.out.println ("���ݿ����ʧ��");
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
     * �޸���� insert update delete
     *
     * @param sql sql ���
     * @return �����޸Ľ��
     */
    public boolean executeUpdate (String sql) {
        System.out.println ("�޸����====" + sql);
        if (con == null) {
            creatConnection ();
        }
        try {
            Statement stmt = con.createStatement ();
            int iCount = stmt.executeUpdate (sql);
            System.out.println ("�����ɹ�����Ӱ��ļ�¼��Ϊ" + String.valueOf (iCount));
            return true;
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
        }
        return false;
    }

    /**
     * ��ѯ���
     *
     * @param sql sql���
     * @return ��ѯ���
     */
    public java.sql.ResultSet executeQuery (String sql) {
        System.out.println ("��ѯ���====" + sql);
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
     * �������ӣ��������������
     */
    public void creatConnectionWithTransaction () {
        try {
            con = java.sql.DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "lkhdsg");
            //�ر��Զ��ύ�����ʧ����ô�ͻع�
            con.setAutoCommit (false);
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("creatConnectionError!");
        }
    }

    /**
     * ��ѯ���
     *
     * @param sql sql ���
     * @return ���ز�ѯ���
     */
    public boolean executeUpdateWithTransaction (String sql) throws SQLException {
        if (con == null) {
            //����һ���������������
            creatConnectionWithTransaction ();
        }
        try {
            Statement stmt = con.createStatement ();
            int iCount = stmt.executeUpdate (sql);
            System.out.println ("�����ɹ�����Ӱ��ļ�¼��Ϊ" + String.valueOf (iCount));
            con.commit ();
            return true;
        } catch (SQLException e) {
            System.out.println (e.getMessage ());
            System.out.println ("����ʧ�ܣ�Ԫ�����Ѿ��ع�");
            con.rollback ();
        }
        return false;
    }

    /**
     * �����ݿ����Դ�����ͷ�
     *
     * @param connection ���ݿ������
     * @param statement  ���ݿ��sql����ʵ�壬������ɾ�Ĳ�Ķ���
     * @param resultSet  ��������ݿ�Ĳ�ѯ���ԣ���ô�ͻ�����������
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
