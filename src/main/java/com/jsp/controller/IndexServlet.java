package com.jsp.controller;

import com.jsp.dao.*;
import com.jsp.form.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArticleDao articleDao = new ArticleDao ();
        String method = req.getParameter ("method");
        if (method == null) {
            //��һ�μ���
            loading (req, resp, articleDao);
        } else if ("get".equals (method)) {
            get (req, resp);
        } else if ("checkType".equals (method)) {
            checkType (req, resp);
        } else if ("addVote".equals (method)) {
            addVote (req, resp);
        } else if ("like".equals (method)) {
            like (req, resp);
        }else if ("collection".equals (method)) {
            collection (req, resp);
        }else if ("userInfo".equals (method)) {
            userInfo (req, resp,articleDao);
        }else if ("userCollection".equals (method)) {
            userCollection (req, resp,articleDao);
        }else if ("search".equals (method)) {
            search (req, resp);
        }
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet (req, resp);
    }


    public void userCollection (HttpServletRequest req, HttpServletResponse resp, ArticleDao articleDao) throws IOException {
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        if(null==user){
            resp.sendRedirect (req.getContextPath () + "/login.jsp");
            return;
        }

        String userId = req.getParameter ("userId");
        DataDao dataDao=new DataDao();
        String sql=" userId="+userId+" and type=2";
        List<DataForm> dataForms = dataDao.queryDataWhere(sql);

        if(!dataForms.isEmpty()){


        StringBuilder in= new StringBuilder("(");
        for (DataForm dataForm : dataForms) {
            in.append(dataForm.getaId()).append(",");
        }
        in.delete(in.length()-1,in.length());
        in.append(")");

        req.getSession ().setAttribute ("pageActive", true);
        String currentPage = req.getParameter ("currentPage");

        if(null==currentPage) currentPage="0";
        int page=Integer.parseInt(currentPage)*4;
        //��ѯȫ���Ĳ�������
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1 and id in "+in+" order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //���ժҪ����Ϊ���µ�ǰ20����
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }

        //���������������ǰ�˼����ҳ
        int totalPage = articleDao.queryArticle (null).size ();
        //����ֵ

        req.getSession ().setAttribute ("total", totalPage);
        req.getSession ().setAttribute ("articles", articles);
        req.getSession ().setAttribute ("currentPage", Integer.parseInt (currentPage));
        }else {
            req.getSession ().setAttribute ("total", 0);
            req.getSession ().setAttribute ("articles", new ArrayList<>());
            req.getSession ().setAttribute ("currentPage", 4);
        }

        resp.sendRedirect (req.getContextPath () + "/collection.jsp");
    }



    public void userInfo (HttpServletRequest req, HttpServletResponse resp, ArticleDao articleDao) throws IOException {
        UserForm user = (UserForm) req.getSession().getAttribute("user");
        if(null==user){
            resp.sendRedirect (req.getContextPath () + "/login.jsp");
            return;
        }

        req.getSession ().setAttribute ("pageActive", true);
        String currentPage = req.getParameter ("currentPage");
        String userId = req.getParameter ("userId");


        if(null==currentPage) currentPage="0";
        int page=Integer.parseInt(currentPage)*4;
        //��ѯȫ���Ĳ�������
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1 and userId="+userId+" order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //���ժҪ����Ϊ���µ�ǰ20����
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        //������еĲ�������
        ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
        List<ArticleTypeForm> list = articleTypeDao.queryArticleType ("");

        //��ùۿ�������������
        List<ArticleForm> hotArticle = articleDao.queryArticleWhere ("status=1 order by phTime desc").subList (0, 3);
        //���������������ǰ�˼����ҳ
        int totalPage = articleDao.queryArticle (null).size ();
        //����ֵ

        UserDao userDao=new UserDao();
        UserForm userForm = userDao.queryUserForm(Integer.valueOf(userId));

        req.getSession ().setAttribute ("total", totalPage);
        req.getSession ().setAttribute ("articles", articles);
        req.getSession ().setAttribute ("userForm", userForm);
        req.getSession ().setAttribute ("currentPage", Integer.parseInt (currentPage));


        resp.sendRedirect (req.getContextPath () + "/userInfo.jsp");
    }



    /**
     * ����ҳ��
     *
     * @param req        ����
     * @param resp       ��Ӧ
     * @param articleDao ���²�ѯ����
     * @throws IOException �쳣
     */
    public void loading (HttpServletRequest req, HttpServletResponse resp, ArticleDao articleDao) throws IOException {
        req.getSession ().setAttribute ("pageActive", true);
        String currentPage = req.getParameter ("currentPage");
        if(null==currentPage) currentPage="0";
        int page=Integer.parseInt(currentPage)*4;
        //��ѯȫ���Ĳ�������
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1  order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //���ժҪ����Ϊ���µ�ǰ20����
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        //������еĲ�������
        ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
        List<ArticleTypeForm> list = articleTypeDao.queryArticleType ("");

        //��ùۿ�������������
        List<ArticleForm> hotArticle = articleDao.queryArticleWhere ("status=1 order by phTime desc").subList (0, 3);
        //���������������ǰ�˼����ҳ
        int totalPage = articleDao.queryArticle (null).size ();
        //����ֵ
        req.getSession ().setAttribute ("total", totalPage);
        req.getSession ().setAttribute ("articles", articles);
        req.getSession ().setAttribute ("hotArticles", hotArticle);
        req.getSession ().setAttribute ("types", list);
        req.getSession ().setAttribute ("currentPage", Integer.parseInt (currentPage));
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }

    /**
     * ���ĳһƪ���͵ľ�������
     *
     * @param req  ����
     * @param resp ��Ӧ
     */

    public void get (HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UserForm user = (UserForm) req.getSession().getAttribute("user");
        if(null==user){
            resp.sendRedirect (req.getContextPath () + "/login.jsp");
            return;
        }

        String id = req.getParameter ("id");
        ArticleDao articleDao = new ArticleDao ();
        ArticleForm form = articleDao.queryArticleForm (Integer.parseInt (id));
        //�������
        VoteDao voteDao = new VoteDao ();
        List<VoteForm> voteForms = voteDao.queryVoteList ("voteLink='" + id + "' ORDER BY voteTime DESC");
        //ֻ��ʾ���µ�10������
        if (voteForms.size () > 5) {
            voteForms = voteForms.subList (0, 5);
        }

        UserDao userDao=new UserDao();
        UserForm blogUser = userDao.queryUserForm(form.getUserId());


        DataDao dataDao=new DataDao();
        String sql="aId="+id+" and type=1";
        int blogCount = dataDao.getBlogCount(sql);

        req.getSession ().setAttribute ("singleArticle", form);
        req.getSession ().setAttribute ("votes", voteForms);
        req.getSession ().setAttribute ("blogUser", blogUser);
        req.getSession ().setAttribute ("blogCount", blogCount);

        resp.sendRedirect (req.getContextPath () + "/post.jsp");
    }


    public void like (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = Integer.valueOf(req.getParameter ("userId"));
        Integer aId = Integer.valueOf(req.getParameter ("aId"));
        DataDao dataDao = new DataDao ();

        String sql=" aid="+aId+" and userId="+userId+" and type=1";
        List<DataForm> list = dataDao.queryDataWhere(sql);

        if(!list.isEmpty()){
            resp.getWriter ().print (false);
            return;
        }

        DataForm data=new DataForm(userId,aId,1);
        dataDao.addData(data);

        resp.getWriter ().print (true);
    }


    public void collection (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = Integer.valueOf(req.getParameter ("userId"));
        Integer aId = Integer.valueOf(req.getParameter ("aId"));
        DataDao dataDao = new DataDao ();

        String sql=" aid="+aId+" and userId="+userId+" and type=2";
        List<DataForm> list = dataDao.queryDataWhere(sql);

        if(!list.isEmpty()){
           dataDao.operationData("ɾ��",list.get(0));
            resp.getWriter ().print (false);
            return;
        }

        DataForm data=new DataForm(userId,aId,2);
        dataDao.addData(data);

        resp.getWriter ().print (true);
    }

    /**
     * ���ݲ������� ��ѯ����
     *
     * @param req  ����
     * @param resp ��Ӧ
     */
    public void checkType (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeid = req.getParameter ("typeid");
        ArticleDao articleDao = new ArticleDao ();
        List<ArticleForm> articleForms = articleDao.queryArticleWhere ("status=1 and typeID='" + typeid + "'  order by number desc");
        for (ArticleForm articleForm : articleForms) {
            //���ժҪ����Ϊ���µ�ǰ20����
            if (articleForm.getContent ().length () > 20) {
                articleForm.setContent (articleForm.getContent ().substring (0, 20));
            }
        }
        //���ҳ������ͷ�ҳ
        if (articleForms.size () > 4) {
            articleForms = articleForms.subList (0, 4);
        }
        //��ѯ�������࣬�Ͳ���ʹ�÷�ҳ
        req.getSession ().setAttribute ("pageActive", false);
        req.getSession ().setAttribute ("articles", articleForms);
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }

    /**
     * ���һ������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void addVote (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String voteLink = req.getParameter ("id");
        String voteName = req.getParameter ("username");
        String email = req.getParameter ("email");
        String voteContent = req.getParameter ("voteContent");
        //������롣�ַ�ת�������Ļ�����
        voteName = new String (voteName.getBytes ("ISO-8859-1"), "utf-8");
        voteContent = new String (voteContent.getBytes ("ISO-8859-1"), "utf-8");
        //�������
        VoteForm voteForm = new VoteForm ();
        voteForm.setVoteContent (voteContent);
        voteForm.setVoteName (voteName);
        voteForm.setVoteLink (voteLink);
        voteForm.setVoteTime (new SimpleDateFormat ("yyyy��MM��dd�� EEEE").format (new Date ()));
        voteForm.setEmail (email);
        VoteDao voteDao = new VoteDao ();
        boolean b = voteDao.addVote (voteForm);
        ArticleDao articleDao = new ArticleDao ();
        ArticleForm form = articleDao.queryArticleForm (Integer.parseInt (voteLink));
        if (b) {
            //���������ӳɹ��Ļ�����ô��������������1
            articleDao.operationArticle ("addVote", form);
        }
        //�������
        List<VoteForm> voteForms = voteDao.queryVoteList ("voteLink='" + voteLink + "' ORDER BY voteTime DESC");
        //ֻ��ʾ���µ�10������
        if (voteForms.size () > 10) {
            voteForms = voteForms.subList (0, 10);
        }
        req.getSession ().setAttribute ("singleArticle", form);
        req.getSession ().setAttribute ("votes", voteForms);
        resp.sendRedirect (req.getContextPath () + "/post.jsp");
    }

    /**
     * ҳ����������
     *
     * @param req  ����
     * @param resp ��Ӧ
     * @throws IOException �쳣
     */
    public void search (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //�رշ�ҳ����
        req.getSession ().setAttribute ("pageActive", false);
        String search = req.getParameter ("search");
        ArticleDao articleDao = new ArticleDao ();
        search = new String (search.getBytes ("ISO-8859-1"), "utf-8");
        List<ArticleForm> articleForms = articleDao.queryArticleWhere ("status=1 and title like '" + "%" + search + "%'");
        for (ArticleForm article : articleForms) {
            //���ժҪ����Ϊ���µ�ǰ20����
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        System.out.println (articleForms);
        req.getSession ().setAttribute ("articles", articleForms);
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }
}
