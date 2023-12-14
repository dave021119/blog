package com.jsp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsp.dao.ArticleDao;
import com.jsp.dao.ArticleTypeDao;
import com.jsp.dao.VoteDao;
import com.jsp.form.ArticleForm;
import com.jsp.form.ArticleTypeForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TypeServlet extends HttpServlet {
    //需要用到的实体类先实例化
    ArticleDao articleDao = new ArticleDao ();
    ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
    VoteDao voteDao = new VoteDao ();

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter ("method");
        if (method == null) {
            loading (req, resp);
        } else if ("del".equals (method)) {
            del (req, resp);
        } else if ("update".equals (method)) {
            update (req, resp);
        } else if ("add".equals (method)) {
            addType (req, resp);
        }
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet (req, resp);
    }

    /**
     * 加载页面初始化
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
        List<ArticleTypeForm> articleForms = this.articleTypeDao.queryArticleType (" where 1=1  limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        //接收的数据
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (articleForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        //获得数据总数
        jsonObject.put ("count", articleTypeDao.queryArticleType (" where 1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * 删除一个博客种类
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        //获得要删除的一条数据
        ArticleTypeForm type = this.articleTypeDao.queryArticleType (" where id=" + id).get (0);
        //删除这篇博客种类以后的后续，只要是带有这篇文章的就要设置为未分类
        List<ArticleForm> articleForms = this.articleDao.queryArticleWhere (" typeId=" + id);
//        ArticleTypeForm notype = this.articleTypeDao.queryArticleType (" where id=8").get (0);
//        for (ArticleForm articleForm : articleForms) {
//            articleForm.setTypeId (8);
//            this.articleDao.updateArticle (articleForm);
//            //未分类上面的数据+1
//            notype.setNumber (notype.getNumber () + 1);
//        }
//        boolean update = this.articleTypeDao.update (notype);
        //在有关的种类数量上面-1
        boolean del = this.articleTypeDao.operationArticleType ("删除", type);
        PrintWriter writer = resp.getWriter ();
        writer.print (del );
    }

    /**
     * 更新一个种类
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleTypeForm articleForm = JSONObject.parseObject (data, ArticleTypeForm.class);
        PrintWriter writer = resp.getWriter ();
        //修改数据
        writer.print (this.articleTypeDao.update (articleForm));
    }

    /**
     * 添加一个博客种类
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void addType (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleTypeForm articleForm = JSONObject.parseObject (data, ArticleTypeForm.class);
        PrintWriter writer = resp.getWriter ();
        //修改数据
        writer.print (this.articleTypeDao.add (articleForm));
    }
}
