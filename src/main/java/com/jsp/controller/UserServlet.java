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
 * 用户界面
 */
public class UserServlet extends HttpServlet {
    //先初始化项目
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
     * 加载页面
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void loading (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = req.getParameter ("page");
        String limit = req.getParameter ("limit");
        resp.setContentType ("text/html;charset=UTF-8");
        JSONObject jsonObject = new JSONObject ();

        List<UserForm>  UserForms = this.userDao.
                queryUserWhere ("1=1  limit " +
                        (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);

        //数据传递
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (UserForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");


        //获得数据的总数
        jsonObject.put ("count", userDao.queryUserWhere ("1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * 删除一篇用户
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        UserForm consumerForm = this.userDao.queryUserForm (Integer.parseInt (id));
        consumerForm.setId (Integer.parseInt (id));


        boolean del = this.userDao.operationUser ("删除", consumerForm);

        PrintWriter writer = resp.getWriter ();
        writer.print (del);
    }

    /**
     * 批量删除
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void dels (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("Users");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        //解析前端传来的json字符串
        List<UserForm> UserForms = JSONArray.parseArray (data, UserForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_User?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456789");
            //设置自动提交为false
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //循环删除
            for (UserForm ConsumerForm : UserForms) {
                statement.executeUpdate ("delete from tb_consumer where id='" + ConsumerForm.getId () + "'");
            }
            //数据提交
            connection.commit ();
            //返回给前端的结果true
            flag = true;
        } catch (Exception exception) {
            //如果发送异常
            try {
                assert connection != null;
                connection.rollback ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ();
            }
            exception.printStackTrace ();
            System.out.println ("删除失败,数据回滚");
        } finally {
            //关闭数据库，释放资源
            JDBConnection.release (connection, statement, null);
        }
        PrintWriter writer = resp.getWriter ();
        writer.print (flag);
    }

    /**
     * 更新一篇用户
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        UserForm consumerForm = JSONObject.parseObject (data, UserForm.class);
        //原来的种类-1后来的种类+1
        Integer id = consumerForm.getId ();
        UserForm form = this.userDao.queryUserForm (id);
        consumerForm.setPassword(StrMD5.MD5(consumerForm.getPassword()));

        PrintWriter writer = resp.getWriter ();
        //传递数据 true或者false
        writer.print (new UserDao().updateUser (consumerForm));
    }

    /**
     * 添加一篇用户
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void addUser (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        UserForm consumerForm = JSONObject.parseObject (data, UserForm.class);;
        consumerForm.setPassword(StrMD5.MD5(consumerForm.getPassword()));

        //添加用户
        boolean b = this.userDao.addUser (consumerForm);
        //输出数据
        resp.getWriter ().print ( b);
    }

    /**
     * 用户搜索
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
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
