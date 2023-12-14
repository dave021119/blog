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
        //��������
        JSONArray data = JSONArray.parseArray (JSON.toJSONString (voteForms));
        jsonObject.put ("data", data);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        //�����������
        jsonObject.put ("count", voteForms.size ());
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }

    /**
     * ɾ��һ������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void del (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter ("id");
        VoteForm voteForm = voteDao.queryVoteList (" id=" + id).get (0);
        System.out.println ("����=" + voteForm);
        //���ɾ�����ۣ���ô��Ӧ�Ĳ��͵�����������Ҫ-1
        ArticleForm articleForm = this.articleDao.queryArticleWhere (" id=" + voteForm.getVoteLink ()).get (0);
        System.out.println ("********************" + articleForm);
        boolean b = this.articleDao.updateArticleVote (articleForm);
        //ɾ������
        boolean delVote = voteDao.delVote (voteForm);
        PrintWriter writer = resp.getWriter ();
        writer.print (b && delVote);
    }

    /**
     * ����ɾ������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void dels (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter ("data");
        Connection connection = null;
        Statement statement = null;
        boolean flag = false;
        System.out.println ("����ɾ������=" + data);
        //���ݹ�����json�ַ����������ݽ��н���,��������������ɾ������������ʹ������һ����һ������ʧ����ô�ͻع�
        List<VoteForm> voteForms = JSONArray.parseArray (data, VoteForm.class);
        try {
            connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/jsp_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai", "root",
                    "123456");
            //�����Զ��ύΪfalse
            connection.setAutoCommit (false);
            statement = connection.createStatement ();
            //����ɾ��
            for (VoteForm voteForm : voteForms) {
                statement.executeUpdate ("delete from tb_vote where id='" + voteForm.getId () + "'");
                //��Ӧ���͵���������-1
                statement.executeUpdate ("UPDATE tb_article SET voteNumber=voteNumber-1 WHERE id=" + voteForm.getVoteLink ());
            }
            //�ύ����
            connection.commit ();
            //û���쳣����true
            flag = true;
        } catch (Exception exception) {
            //���ʧ�����Զ��ع�
            try {
                assert connection != null;
                connection.rollback ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ();
            }
            exception.printStackTrace ();
            System.out.println ("�е�����ɾ��ʧ�ܣ����ݻع�");
        } finally {
            //����ͷ���Դ
            JDBConnection.release (connection, statement, null);
        }
        PrintWriter writer = resp.getWriter ();
        writer.print (flag);
    }

    /**
     * ������������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void search (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType ("text/html;charset=UTF-8");
        String data = req.getParameter ("data");
        //���ǰ�˴���������
        JSONObject js = JSONObject.parseObject (data);
        String id = js.getString ("id");
        //��ĿΪ�գ�ֻ�ò���ID
        StringBuilder sql = new StringBuilder ("1=1");
        if (! "".equals (id)) {
            sql.append (" and voteLink=").append (id);
        }
        List<VoteForm> voteForms = this.voteDao.queryVoteList (sql.toString ());
        //����������� ��������
        JSONArray fallback = JSONArray.parseArray (JSON.toJSONString (voteForms));
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("data", fallback);
        jsonObject.put ("code", 0);
        jsonObject.put ("msg", "");
        jsonObject.put ("count", voteForms.size ());
        //�������
        PrintWriter writer = resp.getWriter ();
        writer.print (jsonObject.toJSONString ());
    }
}
