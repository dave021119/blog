package com.jsp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsp.dao.ArticleDao;
import com.jsp.dao.ArticleTypeDao;
import com.jsp.dao.VoteDao;
import com.jsp.form.ArticleForm;
import com.jsp.form.ArticleTypeForm;
import com.jsp.form.UserForm;
import com.jsp.form.VoteForm;
import com.jsp.tool.JDBConnection;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * �û�����
 */
public class AdminServlet extends HttpServlet {
    //�ȳ�ʼ����Ŀ
    ArticleDao articleDao = new ArticleDao ();
    ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
    VoteDao voteDao = new VoteDao ();


    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost (req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath ();
        if ("/admin/blog".equals (servletPath)) {
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
                addBlog (req, resp);
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
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        List<ArticleForm> articleForms;
        if(user.getId()==1){
            articleForms = this.articleDao.queryArticleWhere ("1=1  order by phTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        }else {
             articleForms = this.articleDao.queryArticleWhere ("1=1 and userId="+ user.getId()+" order by phTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        }
        //���ݴ���
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (articleForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");

        //��ѯ���е�����
        List<ArticleTypeForm> types = this.articleTypeDao.queryArticleType ("");
        req.getSession ().setAttribute ("types", types);
        //������ݵ�����
        jsonObject.put ("count", articleDao.queryArticleWhere ("1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * ɾ��һƪ����
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        ArticleForm articleForm = this.articleDao.queryArticleForm (Integer.parseInt (id));
        articleForm.setId (Integer.parseInt (id));

        Integer typeId = articleForm.getTypeId ();
        Integer formId = articleForm.getId ();
        boolean del = this.articleDao.operationArticle ("ɾ��", articleForm);
        //ɾ��һƪ���Ͷ��㻰������Ӧ�������-1
        List<ArticleTypeForm> articleTypeForms = this.articleTypeDao.queryArticleType (" where id='" + typeId + "'");
        ArticleTypeForm typeForm = articleTypeForms.get (0);
        typeForm.setNumber (typeForm.getNumber () - 1);
        this.articleTypeDao.update (typeForm);
        //ɾ�������Ժ���������ҲҪɾ��
        List<VoteForm> voteForms = this.voteDao.queryVoteList ("voteLink='" + formId + "'");
        for (VoteForm voteForm : voteForms) {
            voteDao.delVote (voteForm);
        }
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
        String data = req.getParameter ("blogs");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        //����ǰ�˴�����json�ַ���
        List<ArticleForm> articleForms = JSONArray.parseArray (data, ArticleForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456");
            //�����Զ��ύΪfalse
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //ѭ��ɾ��
            for (ArticleForm articleForm : articleForms) {
                statement.executeUpdate ("delete from tb_article where id='" + articleForm.getId () + "'");
                //��������-1
                statement.executeUpdate ("UPDATE tb_articletype SET number=number-1 WHERE id=" + articleForm.getTypeId ());
                //ɾ����Ӧ������
                statement.executeUpdate ("delete from tb_vote where voteLink='" + articleForm.getId () + "'");
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
     * ����һƪ����
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleForm articleForm = JSONObject.parseObject (data, ArticleForm.class);
        //ԭ��������-1����������+1
        Integer id = articleForm.getId ();
        ArticleForm form = this.articleDao.queryArticleForm (id);
        //�����ı��������ID
        if (! form.getTypeId ().equals (articleForm.getTypeId ())) {
            //ԭ��������-1
            ArticleTypeForm typeForm = this.articleTypeDao.queryArticleType (" where id=" + form.getTypeId ()).get (0);
            typeForm.setNumber (typeForm.getNumber () - 1);
            this.articleTypeDao.update (typeForm);
            //����������+1
            ArticleTypeForm typeForm1 = this.articleTypeDao.queryArticleType (" where id=" + articleForm.getTypeId ()).get (0);
            typeForm1.setNumber (typeForm1.getNumber () + 1);
            this.articleTypeDao.update (typeForm1);
        }
        PrintWriter writer = resp.getWriter ();
        //�������� true����false
        writer.print (new ArticleDao ().updateArticle (articleForm));
    }

    /**
     * ���һƪ����
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void addBlog (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleForm articleForm = JSONObject.parseObject (data, ArticleForm.class);
        articleForm.setPhTime (new SimpleDateFormat ("yyyy��MM��dd�� EEEE").format (new Date ()));
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        articleForm.setUserId(user.getId());
        //���ò��ͳ�ʼ�Ķ���Ϊ0
        articleForm.setNumber (0);
        //��Ӧ���������+1
        ArticleTypeForm typeForm = this.articleTypeDao.queryArticleType (" where id='" + articleForm.getTypeId () + "'").get (0);
        typeForm.setNumber (typeForm.getNumber () + 1);
        boolean update = this.articleTypeDao.update (typeForm);
        //��Ӳ���
        boolean b = this.articleDao.addBlog (articleForm);
        //�������
        resp.getWriter ().print (update && b);
    }

    /**
     * ��������
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
        String typeId = js.getString ("label");
        String title = js.getString ("title");


        StringBuilder sql = new StringBuilder ("1=1");
        if (! "".equals (id)) {
            sql.append (" and id=").append (id);
        }
        if (! "".equals (typeId)) {
            sql.append (" and typeId=").append (typeId);
        }
        if (! "".equals (title)) {
            sql.append (" and title like '%").append (title).append ("%'");
        }
        List<ArticleForm> articleForms = this.articleDao.queryArticleWhere (sql.toString ());

        JSONArray fallback = JSONArray.parseArray (JSON.toJSONString (articleForms));
        System.out.println (fallback);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("data", fallback);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        jsonObject.put ("count", articleForms.size ());

        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }
}
