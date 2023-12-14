package com.jsp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsp.dao.UserDao;
import com.jsp.form.UserForm;
import com.jsp.tool.JDBConnection;
import com.jsp.tool.StrMD5;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * �û�����
 */
public class UserServlet extends HttpServlet {
    //�ȳ�ʼ����Ŀ
    UserDao userDao = new UserDao ();



    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost (req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath ();
        if ("/admin/user".equals (servletPath)) {
            String method = req.getParameter ("method");
            if (method == null) {
                loading (req, resp);
            } else if ("del".equals (method)) {
                del (req, resp);
            } else if ("dels".equals (method)) {
                dels (req, resp);
            } else if ("update".equals (method)) {
                update (req, resp);
            } else if ("add".equals (method)) {
                addUser (req, resp);
            } else if ("search".equals (method)) {
                search (req, resp);
            }
        }
    }

    /**
     * ����ҳ��
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void loading (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = req.getParameter ("page");
        String limit = req.getParameter ("limit");
        resp.setContentType ("text/html;charset=UTF-8");
        JSONObject jsonObject = new JSONObject ();

        List<UserForm>  UserForms = this.userDao.
                queryUserWhere ("1=1  limit " +
                        (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);

        //���ݴ���
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (UserForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");


        //������ݵ�����
        jsonObject.put ("count", userDao.queryUserWhere ("1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * ɾ��һƪ�û�
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        UserForm consumerForm = this.userDao.queryUserForm (Integer.parseInt (id));
        consumerForm.setId (Integer.parseInt (id));


        boolean del = this.userDao.operationUser ("ɾ��", consumerForm);

        PrintWriter writer = resp.getWriter ();
        writer.print (del);
    }

    /**
     * ����ɾ��
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void dels (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("Users");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        //����ǰ�˴�����json�ַ���
        List<UserForm> UserForms = JSONArray.parseArray (data, UserForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_User?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456789");
            //�����Զ��ύΪfalse
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //ѭ��ɾ��
            for (UserForm ConsumerForm : UserForms) {
                statement.executeUpdate ("delete from tb_consumer where id='" + ConsumerForm.getId () + "'");
            }
            //�����ύ
            connection.commit ();
            //���ظ�ǰ�˵Ľ��true
            flag = true;
        } catch (Exception exception) {
            //��������쳣
            try {
                assert connection != null;
                connection.rollback ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ();
            }
            exception.printStackTrace ();
            System.out.println ("ɾ��ʧ��,���ݻع�");
        } finally {
            //�ر����ݿ⣬�ͷ���Դ
            JDBConnection.release (connection, statement, null);
        }
        PrintWriter writer = resp.getWriter ();
        writer.print (flag);
    }

    /**
     * ����һƪ�û�
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        UserForm consumerForm = JSONObject.parseObject (data, UserForm.class);
        //ԭ��������-1����������+1
        Integer id = consumerForm.getId ();
        UserForm form = this.userDao.queryUserForm (id);
        consumerForm.setPassword(StrMD5.MD5(consumerForm.getPassword()));

        PrintWriter writer = resp.getWriter ();
        //�������� true����false
        writer.print (new UserDao().updateUser (consumerForm));
    }

    /**
     * ���һƪ�û�
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void addUser (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        UserForm consumerForm = JSONObject.parseObject (data, UserForm.class);;
        consumerForm.setPassword(StrMD5.MD5(consumerForm.getPassword()));

        //����û�
        boolean b = this.userDao.addUser (consumerForm);
        //�������
        resp.getWriter ().print ( b);
    }

    /**
     * �û�����
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void search (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType ("text/html;charset=UTF-8");
        String data = req.getParameter ("data");

        JSONObject js = JSONObject.parseObject (data);
        String id = js.getString ("id");



        StringBuilder sql = new StringBuilder ("1=1");
        if (! "".equals (id)) {
            sql.append (" and id=").append (id);
        }

        List<UserForm> UserForms = this.userDao.queryUserWhere (sql.toString ());

        JSONArray fallback = JSONArray.parseArray (JSON.toJSONString (UserForms));
        System.out.println (fallback);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("data", fallback);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        jsonObject.put ("count", UserForms.size ());

        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }
}
