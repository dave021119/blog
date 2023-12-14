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
            //第一次加载
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
        //查询全部的博客文章
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1 and id in "+in+" order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //获得摘要内容为文章的前20个字
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }

        //获得数据总数用于前端计算分页
        int totalPage = articleDao.queryArticle (null).size ();
        //传递值

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
        //查询全部的博客文章
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1 and userId="+userId+" order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //获得摘要内容为文章的前20个字
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        //获得所有的博客种类
        ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
        List<ArticleTypeForm> list = articleTypeDao.queryArticleType ("");

        //获得观看次数最多的文章
        List<ArticleForm> hotArticle = articleDao.queryArticleWhere ("status=1 order by phTime desc").subList (0, 3);
        //获得数据总数用于前端计算分页
        int totalPage = articleDao.queryArticle (null).size ();
        //传递值

        UserDao userDao=new UserDao();
        UserForm userForm = userDao.queryUserForm(Integer.valueOf(userId));

        req.getSession ().setAttribute ("total", totalPage);
        req.getSession ().setAttribute ("articles", articles);
        req.getSession ().setAttribute ("userForm", userForm);
        req.getSession ().setAttribute ("currentPage", Integer.parseInt (currentPage));


        resp.sendRedirect (req.getContextPath () + "/userInfo.jsp");
    }



    /**
     * 加载页面
     *
     * @param req        请求
     * @param resp       响应
     * @param articleDao 文章查询对象
     * @throws IOException 异常
     */
    public void loading (HttpServletRequest req, HttpServletResponse resp, ArticleDao articleDao) throws IOException {
        req.getSession ().setAttribute ("pageActive", true);
        String currentPage = req.getParameter ("currentPage");
        if(null==currentPage) currentPage="0";
        int page=Integer.parseInt(currentPage)*4;
        //查询全部的博客文章
        List<ArticleForm> articles = articleDao.queryArticleWhere ("status=1  order by phTime desc limit "+page+",4");

        for (ArticleForm article : articles) {
            //获得摘要内容为文章的前20个字
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        //获得所有的博客种类
        ArticleTypeDao articleTypeDao = new ArticleTypeDao ();
        List<ArticleTypeForm> list = articleTypeDao.queryArticleType ("");

        //获得观看次数最多的文章
        List<ArticleForm> hotArticle = articleDao.queryArticleWhere ("status=1 order by phTime desc").subList (0, 3);
        //获得数据总数用于前端计算分页
        int totalPage = articleDao.queryArticle (null).size ();
        //传递值
        req.getSession ().setAttribute ("total", totalPage);
        req.getSession ().setAttribute ("articles", articles);
        req.getSession ().setAttribute ("hotArticles", hotArticle);
        req.getSession ().setAttribute ("types", list);
        req.getSession ().setAttribute ("currentPage", Integer.parseInt (currentPage));
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }

    /**
     * 获得某一篇博客的具体内容
     *
     * @param req  请求
     * @param resp 响应
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
        //获得评论
        VoteDao voteDao = new VoteDao ();
        List<VoteForm> voteForms = voteDao.queryVoteList ("voteLink='" + id + "' ORDER BY voteTime DESC");
        //只显示最新的10条评论
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
           dataDao.operationData("删除",list.get(0));
            resp.getWriter ().print (false);
            return;
        }

        DataForm data=new DataForm(userId,aId,2);
        dataDao.addData(data);

        resp.getWriter ().print (true);
    }

    /**
     * 根据博客种类 查询博客
     *
     * @param req  请求
     * @param resp 响应
     */
    public void checkType (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeid = req.getParameter ("typeid");
        ArticleDao articleDao = new ArticleDao ();
        List<ArticleForm> articleForms = articleDao.queryArticleWhere ("status=1 and typeID='" + typeid + "'  order by number desc");
        for (ArticleForm articleForm : articleForms) {
            //获得摘要内容为文章的前20个字
            if (articleForm.getContent ().length () > 20) {
                articleForm.setContent (articleForm.getContent ().substring (0, 20));
            }
        }
        //如果页数过大就分页
        if (articleForms.size () > 4) {
            articleForms = articleForms.subList (0, 4);
        }
        //查询博客种类，就不用使用分页
        req.getSession ().setAttribute ("pageActive", false);
        req.getSession ().setAttribute ("articles", articleForms);
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }

    /**
     * 添加一条评论
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void addVote (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String voteLink = req.getParameter ("id");
        String voteName = req.getParameter ("username");
        String email = req.getParameter ("email");
        String voteContent = req.getParameter ("voteContent");
        //解决乱码。字符转换，中文会乱码
        voteName = new String (voteName.getBytes ("ISO-8859-1"), "utf-8");
        voteContent = new String (voteContent.getBytes ("ISO-8859-1"), "utf-8");
        //添加评论
        VoteForm voteForm = new VoteForm ();
        voteForm.setVoteContent (voteContent);
        voteForm.setVoteName (voteName);
        voteForm.setVoteLink (voteLink);
        voteForm.setVoteTime (new SimpleDateFormat ("yyyy年MM月dd日 EEEE").format (new Date ()));
        voteForm.setEmail (email);
        VoteDao voteDao = new VoteDao ();
        boolean b = voteDao.addVote (voteForm);
        ArticleDao articleDao = new ArticleDao ();
        ArticleForm form = articleDao.queryArticleForm (Integer.parseInt (voteLink));
        if (b) {
            //如果评论添加成功的话，那么他的评论条数加1
            articleDao.operationArticle ("addVote", form);
        }
        //获得评论
        List<VoteForm> voteForms = voteDao.queryVoteList ("voteLink='" + voteLink + "' ORDER BY voteTime DESC");
        //只显示最新的10条评论
        if (voteForms.size () > 10) {
            voteForms = voteForms.subList (0, 10);
        }
        req.getSession ().setAttribute ("singleArticle", form);
        req.getSession ().setAttribute ("votes", voteForms);
        resp.sendRedirect (req.getContextPath () + "/post.jsp");
    }

    /**
     * 页面搜索功能
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 异常
     */
    public void search (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //关闭分页功能
        req.getSession ().setAttribute ("pageActive", false);
        String search = req.getParameter ("search");
        ArticleDao articleDao = new ArticleDao ();
        search = new String (search.getBytes ("ISO-8859-1"), "utf-8");
        List<ArticleForm> articleForms = articleDao.queryArticleWhere ("status=1 and title like '" + "%" + search + "%'");
        for (ArticleForm article : articleForms) {
            //获得摘要内容为文章的前20个字
            if (article.getContent ().length () > 20) {
                article.setContent (article.getContent ().substring (0, 20));
            }
        }
        System.out.println (articleForms);
        req.getSession ().setAttribute ("articles", articleForms);
        resp.sendRedirect (req.getContextPath () + "/index.jsp");
    }
}
