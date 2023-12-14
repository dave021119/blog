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
 * 用户界面
 */
public class AdminServlet extends HttpServlet {
    //先初始化项目
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
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        List<ArticleForm> articleForms;
        if(user.getId()==1){
            articleForms = this.articleDao.queryArticleWhere ("1=1  order by phTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        }else {
             articleForms = this.articleDao.queryArticleWhere ("1=1 and userId="+ user.getId()+" order by phTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);
        }
        //数据传递
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (articleForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");

        //查询所有的种类
        List<ArticleTypeForm> types = this.articleTypeDao.queryArticleType ("");
        req.getSession ().setAttribute ("types", types);
        //获得数据的总数
        jsonObject.put ("count", articleDao.queryArticleWhere ("1=1").size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * 删除一篇博客
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        ArticleForm articleForm = this.articleDao.queryArticleForm (Integer.parseInt (id));
        articleForm.setId (Integer.parseInt (id));

        Integer typeId = articleForm.getTypeId ();
        Integer formId = articleForm.getId ();
        boolean del = this.articleDao.operationArticle ("删除", articleForm);
        //删除一篇博客二点话，他对应的种类就-1
        List<ArticleTypeForm> articleTypeForms = this.articleTypeDao.queryArticleType (" where id='" + typeId + "'");
        ArticleTypeForm typeForm = articleTypeForms.get (0);
        typeForm.setNumber (typeForm.getNumber () - 1);
        this.articleTypeDao.update (typeForm);
        //删除博客以后，他的评论也要删除
        List<VoteForm> voteForms = this.voteDao.queryVoteList ("voteLink='" + formId + "'");
        for (VoteForm voteForm : voteForms) {
            voteDao.delVote (voteForm);
        }
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
        String data = req.getParameter ("blogs");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        //解析前端传来的json字符串
        List<ArticleForm> articleForms = JSONArray.parseArray (data, ArticleForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456");
            //设置自动提交为false
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //循环删除
            for (ArticleForm articleForm : articleForms) {
                statement.executeUpdate ("delete from tb_article where id='" + articleForm.getId () + "'");
                //博客数量-1
                statement.executeUpdate ("UPDATE tb_articletype SET number=number-1 WHERE id=" + articleForm.getTypeId ());
                //删除对应的评论
                statement.executeUpdate ("delete from tb_vote where voteLink='" + articleForm.getId () + "'");
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
     * 更新一篇博客
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void update (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleForm articleForm = JSONObject.parseObject (data, ArticleForm.class);
        //原来的种类-1后来的种类+1
        Integer id = articleForm.getId ();
        ArticleForm form = this.articleDao.queryArticleForm (id);
        //如果你改变了种类的ID
        if (! form.getTypeId ().equals (articleForm.getTypeId ())) {
            //原来的种类-1
            ArticleTypeForm typeForm = this.articleTypeDao.queryArticleType (" where id=" + form.getTypeId ()).get (0);
            typeForm.setNumber (typeForm.getNumber () - 1);
            this.articleTypeDao.update (typeForm);
            //后来的种类+1
            ArticleTypeForm typeForm1 = this.articleTypeDao.queryArticleType (" where id=" + articleForm.getTypeId ()).get (0);
            typeForm1.setNumber (typeForm1.getNumber () + 1);
            this.articleTypeDao.update (typeForm1);
        }
        PrintWriter writer = resp.getWriter ();
        //传递数据 true或者false
        writer.print (new ArticleDao ().updateArticle (articleForm));
    }

    /**
     * 添加一篇博客
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void addBlog (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        ArticleForm articleForm = JSONObject.parseObject (data, ArticleForm.class);
        articleForm.setPhTime (new SimpleDateFormat ("yyyy年MM月dd日 EEEE").format (new Date ()));
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        articleForm.setUserId(user.getId());
        //设置博客初始阅读量为0
        articleForm.setNumber (0);
        //对应种类的数量+1
        ArticleTypeForm typeForm = this.articleTypeDao.queryArticleType (" where id='" + articleForm.getTypeId () + "'").get (0);
        typeForm.setNumber (typeForm.getNumber () + 1);
        boolean update = this.articleTypeDao.update (typeForm);
        //添加博客
        boolean b = this.articleDao.addBlog (articleForm);
        //输出数据
        resp.getWriter ().print (update && b);
    }

    /**
     * 博客搜索
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
