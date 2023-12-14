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
    //��Ҫ�õ���ʵ������ʵ����
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
     * ����ҳ���ʼ��
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
        List<ArticleTypeForm> articleForms = this.articleTypeDao.queryArticleType (" where 1=1  limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        //���յ�����
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (articleForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        //�����������
        jsonObject.put ("count", articleTypeDao.queryArticleType (" where 1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * ɾ��һ����������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        //���Ҫɾ����һ������
        ArticleTypeForm type = this.articleTypeDao.queryArticleType (" where id=" + id).get (0);
        //ɾ����ƪ���������Ժ�ĺ�����ֻҪ�Ǵ�����ƪ���µľ�Ҫ����Ϊδ����
        List<ArticleForm> articleForms = this.articleDao.queryArticleWhere (" typeId=" + id);
//        ArticleTypeForm notype = this.articleTypeDao.queryArticleType (" where id=8").get (0);
//        for (ArticleForm articleForm : articleForms) {
//            articleForm.setTypeId (8);
//            this.articleDao.updateArticle (articleForm);
//            //δ�������������+1
//            notype.setNumber (notype.getNumber () + 1);
//        }
//        boolean update = this.articleTypeDao.update (notype);
        //���йص�������������-1
        boolean del = this.articleTypeDao.operationArticleType ("ɾ��", type);
        PrintWriter writer = resp.getWriter ();
        writer.print (del );
    }

    /**
     * ����һ������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleTypeForm articleForm = JSONObject.parseObject (data, ArticleTypeForm.class);
        PrintWriter writer = resp.getWriter ();
        //�޸�����
        writer.print (this.articleTypeDao.update (articleForm));
    }

    /**
     * ���һ����������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void addType (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleTypeForm articleForm = JSONObject.parseObject (data, ArticleTypeForm.class);
        PrintWriter writer = resp.getWriter ();
        //�޸�����
        writer.print (this.articleTypeDao.add (articleForm));
    }
}
