package com.jsp.form;

public class ArticleForm {
    //����ID
    private Integer id = - 1;
    //��������
    private Integer typeId = - 1;
    //������Ŀ
    private String title = "";
    //��������
    private String content = "";
    //����ʱ��
    private String phTime = "";
    //�ۿ�����
    private Integer number = - 1;
    //ͼƬ����
    private String image;
    //��������
    private int voteNumber;
    //�Ƿ���ã����õĻ�����1
    private int status;

    private int userId;

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getVoteNumber () {
        return voteNumber;
    }

    public void setVoteNumber (int voteNumber) {
        this.voteNumber = voteNumber;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public ArticleForm () {
    }

    public String getContent () {
        return this.content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public Integer getId () {
        return this.id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public Integer getNumber () {
        return this.number;
    }

    public void setNumber (Integer number) {
        this.number = number;
    }

    public String getPhTime () {
        return this.phTime;
    }

    public void setPhTime (String phTime) {
        this.phTime = phTime;
    }

    public String getTitle () {
        return this.title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public Integer getTypeId () {
        return this.typeId;
    }

    public void setTypeId (Integer typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString () {
        return "ArticleForm{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", phTime='" + phTime + '\'' +
                ", number=" + number +
                ", image='" + image + '\'' +
                ", voteNumber=" + voteNumber +
                ", status=" + status +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
