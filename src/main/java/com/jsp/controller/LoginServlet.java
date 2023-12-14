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
     * ��¼��֤
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String data = req.getParameter("data");
        JSONObject js = JSONObject.parseObject(data);
        //�����֤����Ϣ
        int msg = 0;
        JSONObject jsonObject = new JSONObject();

        String username = js.getString("username");
        String password = js.getString("password");
        UserForm login = this.consumerDao.login(username, StrMD5.MD5(password));
        if (login.getId() == 0) {
            msg = 405;
        } else {
            //�����ݷŵ�session���� ��������������֤��
            CaptchaUtil.clear(req);
            //session
            req.getSession().setAttribute("articleNum", this.articleDao.getCount());
            req.getSession().setAttribute("typeNum", this.articleTypeDao.getCount());
            req.getSession().setAttribute("voteNum", this.voteDao.getCount());
            req.getSession().setAttribute("user", login);
        }
        //���⴫������
        jsonObject.put("code", 0);
        jsonObject.put("msg", JSONObject.toJSONString(msg));
        PrintWriter writer = resp.getWriter();
        writer.print(jsonObject.toJSONString());
    }

    /**
     * �˳���¼
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //���session,�˵���ҳ
        req.getSession().removeAttribute("user");
        System.out.println("�˳�");
        resp.sendRedirect(req.getContextPath() + "/index");
    }

    /**
     * �û�ע��
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter("data");
        JSONObject js = JSONObject.parseObject(data);
        //�������
        String cellphone = js.getString("cellphone");
        String password = js.getString("password");
        String nickname = js.getString("nickname");
        int msg = 0;
        UserForm consumerForm = new UserForm();
        //ʹ�õ绰������Ϊ�˻�
        consumerForm.setAccount(cellphone);
        consumerForm.setPassword(password);
        consumerForm.setName(nickname);
        consumerForm.setRole("2");
        consumerForm.setCreateTime(new Date());
        if (!this.consumerDao.addConsumerForm(consumerForm)) {
            //���ʧ��
            msg = 1;
        }
        //�����֤����Ϣ
        JSONObject jsonObject = new JSONObject();
        //���⴫������
        jsonObject.put("code", 0);
        jsonObject.put("msg", JSONObject.toJSONString(msg));
        PrintWriter writer = resp.getWriter();
        writer.print(jsonObject.toJSONString());
    }





}
