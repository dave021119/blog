package com.jsp.form;

public class VoteForm {
    Integer id = - 1;
    String voteName = null;
    String voteTime = null;
    String voteContent = null;
    String voteLink = null;
    String email = null;

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getVoteLink () {
        return voteLink;
    }

    public void setVoteLink (String voteLink) {
        this.voteLink = voteLink;
    }

    public VoteForm () {
    }

    public Integer getId () {
        return this.id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getVoteName () {
        return this.voteName;
    }

    public void setVoteName (String voteName) {
        this.voteName = voteName;
    }

    public String getVoteContent () {
        return this.voteContent;
    }

    public void setVoteContent (String voteContent) {
        this.voteContent = voteContent;
    }

    public String getVoteTime () {
        return this.voteTime;
    }

    public void setVoteTime (String voteTime) {
        this.voteTime = voteTime;
    }

    @Override
    public String toString () {
        return "VoteForm{" +
                "id=" + id +
                ", voteName='" + voteName + '\'' +
                ", voteTime='" + voteTime + '\'' +
                ", voteContent='" + voteContent + '\'' +
                ", voteLink='" + voteLink + '\'' +
                '}';
    }
}
