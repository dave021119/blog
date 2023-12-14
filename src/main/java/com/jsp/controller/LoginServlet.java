package com.jsp.controller;


import com.alibaba.fastjson.JSONObject;
import com.jsp.dao.*;
import com.jsp.form.UserForm;
import com.jsp.tool.StrMD5;
import com.wf.captcha.utils.CaptchaUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    ConsumerDao consumerDao = new ConsumerDao();
    ArticleDao articleDao = new ArticleDao();
    ArticleTypeDao articleTypeDao = new ArticleTypeDao();
    VoteDao voteDao = new VoteDao();
    Map<String, String> map = new HashMap<>();
    private String userAccount = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("login".equals(method)) {
            login(req, resp);
        } else if ("logout".equals(method)) {
            logout(req, resp);
        } else if ("add".equals(method)) {
            add(req, resp);
        }  else {
            req.getSession().setAttribute("adminCount", this.consumerDao.getCount());
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp");
        }
    }

    /**
     * 登录验证
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String data = req.getParameter("data");
        JSONObject js = JSONObject.parseObject(data);
        //获得验证码信息
        int msg = 0;
        JSONObject jsonObject = new JSONObject();

        String username = js.getString("username");
        String password = js.getString("password");
        UserForm login = this.consumerDao.login(username, StrMD5.MD5(password));
        if (login.getId() == 0) {
            msg = 405;
        } else {
            //把数据放到session里面 清除数据里面的验证码
            CaptchaUtil.clear(req);
            //session
            req.getSession().setAttribute("articleNum", this.articleDao.getCount());
            req.getSession().setAttribute("typeNum", this.articleTypeDao.getCount());
            req.getSession().setAttribute("voteNum", this.voteDao.getCount());
            req.getSession().setAttribute("user", login);
        }
        //向外传递数据
        jsonObject.put("code", 0);
        jsonObject.put("msg", JSONObject.toJSONString(msg));
        PrintWriter writer = resp.getWriter();
        writer.print(jsonObject.toJSONString());
    }

    /**
     * 退出登录
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //清除session,退到主页
        req.getSession().removeAttribute("user");
        System.out.println("退出");
        resp.sendRedirect(req.getContextPath() + "/index");
    }

    /**
     * 用户注册
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter("data");
        JSONObject js = JSONObject.parseObject(data);
        //获得数据
        String cellphone = js.getString("cellphone");
        String password = js.getString("password");
        String nickname = js.getString("nickname");
        int msg = 0;
        UserForm consumerForm = new UserForm();
        //使用电话号码作为账户
        consumerForm.setAccount(cellphone);
        consumerForm.setPassword(password);
        consumerForm.setName(nickname);
        consumerForm.setRole("2");
        consumerForm.setCreateTime(new Date());
        if (!this.consumerDao.addConsumerForm(consumerForm)) {
            //添加失败
            msg = 1;
        }
        //获得验证码信息
        JSONObject jsonObject = new JSONObject();
        //向外传递数据
        jsonObject.put("code", 0);
        jsonObject.put("msg", JSONObject.toJSONString(msg));
        PrintWriter writer = resp.getWriter();
        writer.print(jsonObject.toJSONString());
    }





}
