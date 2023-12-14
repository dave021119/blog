package com.jsp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsp.dao.ArticleDao;
import com.jsp.dao.ArticleTypeDao;
import com.jsp.dao.VoteDao;
import com.jsp.form.ArticleForm;
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
import java.util.List;

public class CommentServlet extends HttpServlet {
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
        } else if ("dels".equals (method)) {
            dels (req, resp);
        } else if ("search".equals (method)) {
            search (req, resp);
        }
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet (req, resp);
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

        UserForm user = (UserForm) req.getSession().getAttribute("user");
        List<ArticleForm> articleForms = this.articleDao.queryArticleWhere ("1=1 and userId="+ user.getId());

        StringBuilder str=new StringBuilder("(");
        for (ArticleForm articleForm : articleForms) {
            str.append(articleForm.getId()).append(",");
        }
        str.deleteCharAt(str.length()-1);
        str.append(")");
        List<VoteForm> voteForms;
        if(user.getId()==1){
            voteForms = this.voteDao.queryVoteList ("1=1  order by voteTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);

        }else {
            voteForms = this.voteDao.queryVoteList ("1=1 and voteLink in "+str+" order by voteTime desc limit " + (Integer.parseInt (page) - 1) * Integer.parseInt (limit) + "," + limit);

        }
        //传递数据
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (voteForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        //获得数据总数
        jsonObject.put ("count", voteForms.size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * 删除一条评论
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        VoteForm voteForm = voteDao.queryVoteList (" id=" + id).get (0);
        System.out.println ("评论=" + voteForm);
        //如果删除评论，那么对应的博客的评论数量就要-1
        ArticleForm articleForm = this.articleDao.queryArticleWhere (" id=" + voteForm.getVoteLink ()).get (0);
        System.out.println ("********************" + articleForm);
        boolean b = this.articleDao.updateArticleVote (articleForm);
        //删除评论
        boolean delVote = voteDao.delVote (voteForm);
        PrintWriter writer = resp.getWriter ();
        writer.print (b && delVote);
    }

    /**
     * 批量删除数据
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void dels (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        System.out.println ("批量删除评论=" + data);
        //传递过来是json字符串，对数据进行解析,并且由于是批量删除，所以我们使用事务，一旦有一个数据失败那么就回滚
        List<VoteForm> voteForms = JSONArray.parseArray (data, VoteForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456");
            //设置自动提交为false
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //批量删除
            for (VoteForm voteForm : voteForms) {
                statement.executeUpdate ("delete from tb_vote where id='" + voteForm.getId () + "'");
                //对应博客的评论数量-1
                statement.executeUpdate ("UPDATE tb_article SET voteNumber=voteNumber-1 WHERE id=" + voteForm.getVoteLink ());
            }
            //提交数据
            connection.commit ();
            //没有异常返回true
            flag = true;
        } catch (Exception exception) {
            //如果失败了自动回滚
            try {
                assert connection != null;
                connection.rollback ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ();
            }
            exception.printStackTrace ();
            System.out.println ("有的数据删除失败，数据回滚");
        } finally {
            //最后释放资源
            JDBConnection.release (connection, statement, null);
        }
        PrintWriter writer = resp.getWriter ();
        writer.print (flag);
    }

    /**
     * 评论搜索功能
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void search (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType ("text/html;charset=UTF-8");
        String data = req.getParameter ("data");
        //获得前端传来的数据
        JSONObject js = JSONObject.parseObject (data);
        String id = js.getString ("id");
        //题目为空，只用博客ID
        StringBuilder sql = new StringBuilder ("1=1");
        if (! "".equals (id)) {
            sql.append (" and voteLink=").append (id);
        }
        List<VoteForm> voteForms = this.voteDao.queryVoteList (sql.toString ());
        //获得数据总数 返回数据
        JSONArray fallback = JSONArray.parseArray (JSON.toJSONString (voteForms));
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("data", fallback);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        jsonObject.put ("count", voteForms.size ());
        //输出数据
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }
}
